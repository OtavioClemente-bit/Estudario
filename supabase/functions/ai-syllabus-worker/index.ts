import type { AiSyllabusProposal, AiWarning } from "../_shared/contracts.ts";
import {
  emitAiTerminalTelemetry,
  type TerminalAiTelemetry,
} from "../_shared/job-finalizer.ts";
import {
  ProposalValidationError,
  type ProposalValidationLimits,
  type ProposalValidationOptions,
  validateAiSyllabusProposal,
} from "../_shared/proposal-validator.ts";
import {
  createOpenAiProvider,
  type OpenAiProvider,
  OpenAiProviderError,
  type ProviderResponse,
  type ProviderUsage,
  resolveOpenAiModel,
} from "../_shared/openai-provider.ts";
import {
  SYLLABUS_PROMPT_VERSION,
  SYLLABUS_SYSTEM_PROMPT,
  syllabusUserPrompt,
} from "../_shared/prompts/syllabus-v2.ts";
import {
  SYLLABUS_PROMPT_VERSION as LEGACY_SYLLABUS_PROMPT_VERSION,
  SYLLABUS_SYSTEM_PROMPT as LEGACY_SYLLABUS_SYSTEM_PROMPT,
  syllabusUserPrompt as legacySyllabusUserPrompt,
} from "../_shared/prompts/syllabus-v1.ts";
import {
  AI_SYLLABUS_PROPOSAL_SCHEMA,
  AI_SYLLABUS_PROPOSAL_SCHEMA_VERSION,
} from "../_shared/schemas/ai-syllabus-proposal-v2.ts";
import {
  AI_SYLLABUS_PROPOSAL_SCHEMA as LEGACY_AI_SYLLABUS_PROPOSAL_SCHEMA,
  AI_SYLLABUS_PROPOSAL_SCHEMA_VERSION as LEGACY_AI_SYLLABUS_PROPOSAL_SCHEMA_VERSION,
} from "../_shared/schemas/ai-syllabus-proposal-v1.ts";
import {
  AI_SYLLABUS_SOURCE_BUCKET,
  type StorageSourceStore,
  SupabaseStorageSourceStore,
} from "../_shared/storage-source.ts";

export interface Lease {
  owner: string;
  token: string;
  generation: number;
}

function leaseRpcArgs(
  lease: Lease,
): {
  p_lease_owner: string;
  p_lease_token: string;
  p_lease_generation: number;
} {
  return {
    p_lease_owner: lease.owner,
    p_lease_token: lease.token,
    p_lease_generation: lease.generation,
  };
}

export class LeaseLostError extends Error {
  constructor() {
    super("AI_JOB_LEASE_LOST");
    this.name = "LeaseLostError";
  }
}

class CancellationPendingError extends Error {
  constructor() {
    super("AI_JOB_CANCELLATION_PENDING");
  }
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
  targetTitle: string | null;
  openaiResponseId: string | null;
  providerExecutionStartedAt: string | null;
  providerStartOutcome?: string;
  providerQuarantinedAt?: string | null;
  cancellationRequestedAt?: string | null;
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
  claimReconciliation(
    now: Date,
    leaseSeconds: number,
    reconciliationSeconds: number,
  ): Promise<SyllabusWorkerJob | null>;
  completeReconciliation(jobId: string, lease: Lease): Promise<void>;
  failReconciliation(jobId: string, lease: Lease, code: string): Promise<void>;
  claimNext(
    now: Date,
    leaseSeconds: number,
    processingSeconds: number,
  ): Promise<SyllabusWorkerJob | null>;
  assertLease(jobId: string, lease: Lease): Promise<void>;
  markProviderStarted?(jobId: string, lease: Lease): Promise<void>;
  recordProviderStartOutcome(
    jobId: string,
    lease: Lease,
    outcome: "PROVIDER_REJECTED" | "TRANSPORT_AMBIGUOUS" | "RESPONSE_AMBIGUOUS",
  ): Promise<void>;
  persistResponseId(
    jobId: string,
    responseId: string,
    lease: Lease,
  ): Promise<void>;
  recoverResponseId?(jobId: string, responseId: string): Promise<void>;
  finalizeNotSent?(jobId: string, lease: Lease, code: string): Promise<void>;
  reconcileProvider(
    jobId: string,
    lease: Lease,
    recoverable: boolean,
  ): Promise<void>;
  markRetry(jobId: string, lease: Lease): Promise<void>;
  captureUsage(
    jobId: string,
    lease: Lease,
    usage: ProviderUsage | null,
  ): Promise<void>;
  finalizeSuccess(
    jobId: string,
    lease: Lease,
    proposal: AiSyllabusProposal,
    warnings: AiWarning[],
    responseId: string | null,
  ): Promise<void>;
  finalizeFailure(
    jobId: string,
    lease: Lease,
    code: string,
    message: string,
    terminalStatus: "FAILED" | "EXPIRED" | "CANCELLED",
    providerReconciled: boolean,
  ): Promise<void>;
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
  providerDiagnostics?: (event: AiProviderDiagnostic) => void;
  logProviderDiagnostics?: boolean;
}

export interface AiProviderDiagnostic {
  event: "ai_provider_diagnostic";
  jobId: string;
  stage: "start" | "retrieve" | "cancel" | "validation";
  outcome: string;
  code: string;
  providerStatus?: ProviderResponse["status"];
  incompleteReason?: string | null;
  status?: number;
  type?: string | null;
  providerCode?: string | null;
  requestId?: string;
  model?: string;
  message: string;
}

function logProviderTerminalStatus(
  dependencies: SyllabusWorkerDependencies,
  job: SyllabusWorkerJob,
  response: ProviderResponse,
): void {
  try {
    const event: AiProviderDiagnostic = {
      event: "ai_provider_diagnostic",
      jobId: job.id,
      stage: "retrieve",
      outcome: "TERMINAL_RESPONSE",
      code: response.status === "incomplete"
        ? "PROVIDER_INCOMPLETE"
        : response.status === "failed"
        ? "PROVIDER_FAILED"
        : response.status === "expired"
        ? "PROVIDER_EXPIRED"
        : "PROVIDER_CANCELLED",
      providerStatus: response.status,
      ...(response.status === "incomplete"
        ? { incompleteReason: response.incompleteReason }
        : {}),
      message: "Provider response reached a terminal status without a result",
    };
    if (dependencies.logProviderDiagnostics !== false) {
      console.error(JSON.stringify(event));
    }
    dependencies.providerDiagnostics?.(event);
  } catch {
    // Diagnostics must never change the provider/job state.
  }
}

async function providerCall<T>(
  dependencies: SyllabusWorkerDependencies,
  job: SyllabusWorkerJob,
  stage: AiProviderDiagnostic["stage"],
  operation: () => Promise<T>,
): Promise<T> {
  try {
    return await operation();
  } catch (error) {
    if (error instanceof OpenAiProviderError) {
      const event: AiProviderDiagnostic = {
        event: "ai_provider_diagnostic",
        jobId: job.id,
        stage,
        outcome: error.outcome,
        code: error.code,
        ...(error.diagnostics?.status === undefined
          ? {}
          : { status: error.diagnostics.status }),
        ...(error.diagnostics?.type === undefined
          ? {}
          : { type: error.diagnostics.type }),
        ...(error.diagnostics?.code === undefined
          ? {}
          : { providerCode: error.diagnostics.code }),
        ...(error.diagnostics?.requestId === undefined
          ? {}
          : { requestId: error.diagnostics.requestId }),
        ...(error.diagnostics?.model === undefined
          ? {}
          : { model: error.diagnostics.model }),
        message: error.diagnostics?.message ??
          "Provider request could not be completed",
      };
      try {
        if (dependencies.providerDiagnostics) {
          dependencies.providerDiagnostics(event);
        } else console.error(JSON.stringify(event));
      } catch {
        // Diagnostics must never alter provider/job state handling.
      }
    }
    throw error;
  }
}

async function persistAcceptedResponseId(
  dependencies: SyllabusWorkerDependencies,
  job: SyllabusWorkerJob,
  lease: Lease,
  responseId: string,
): Promise<boolean> {
  try {
    await dependencies.jobs.persistResponseId(job.id, responseId, lease);
  } catch {
    if (dependencies.jobs.recoverResponseId) {
      try {
        await dependencies.jobs.recoverResponseId(job.id, responseId);
      } catch {
        // Keep the durable quarantine if both persistence paths fail.
      }
    }
    return false;
  }
  job.openaiResponseId = responseId;
  job.providerStartOutcome = "ACCEPTED";
  job.providerQuarantinedAt = null;
  return true;
}

async function emitTerminalTelemetry(
  dependencies: SyllabusWorkerDependencies,
  job: SyllabusWorkerJob,
  terminalStatus: TerminalAiTelemetry["terminalStatus"],
  usage: ProviderUsage | null = null,
  proposal: AiSyllabusProposal | null = null,
): Promise<void> {
  await emitAiTerminalTelemetry({
    feature: "SYLLABUS_GENERATION",
    userId: job.userId,
    modelVersion: proposal?.modelVersion ?? job.modelVersion ??
      resolveOpenAiModel(dependencies.model),
    promptVersion: proposal?.promptVersion ?? job.promptVersion ??
      SYLLABUS_PROMPT_VERSION,
    schemaVersion: proposal?.schemaVersion ??
      AI_SYLLABUS_PROPOSAL_SCHEMA_VERSION,
    jobId: job.id,
    usage,
    startedAt: job.providerExecutionStartedAt,
    terminalStatus,
    now: dependencies.now,
    sink: dependencies.telemetry,
  });
}

function leaseOf(job: SyllabusWorkerJob): Lease {
  if (
    !job.leaseOwner || !job.leaseToken ||
    !Number.isSafeInteger(job.leaseGeneration)
  ) throw new LeaseLostError();
  return {
    owner: job.leaseOwner,
    token: job.leaseToken,
    generation: job.leaseGeneration,
  };
}

function errorCode(error: unknown): string {
  if (error instanceof OpenAiProviderError) {
    if (error.code === "OPENAI_API_KEY_MISSING") {
      return "OPENAI_API_KEY_MISSING";
    }
    if (error.code === "OPENAI_TIMEOUT") return "PROVIDER_TIMEOUT";
    return error.code;
  }
  const message = error instanceof Error ? error.message : String(error);
  return /^[A-Z][A-Z0-9_]{2,63}$/.test(message) ? message : "PROVIDER_ERROR";
}

function preProviderDefinitive(
  code: string,
  providerStarted: boolean,
  job: SyllabusWorkerJob,
): boolean {
  if (
    providerStarted || job.openaiResponseId !== null ||
    job.providerQuarantinedAt != null ||
    (job.providerStartOutcome !== undefined &&
      job.providerStartOutcome !== "NOT_STARTED")
  ) return false;
  return code === "OPENAI_API_KEY_MISSING" || code === "SOURCE_NOT_FOUND" ||
    code === "SOURCE_HASH_MISMATCH" || code.startsWith("SOURCE_");
}

function deadlineExceeded(job: SyllabusWorkerJob, now: Date): boolean {
  return job.processingDeadlineAt !== null &&
    Date.parse(job.processingDeadlineAt) <= now.getTime();
}

function terminalProviderStatus(status: ProviderResponse["status"]): boolean {
  return status === "failed" || status === "cancelled" ||
    status === "expired" || status === "incomplete";
}

async function cleanupBestEffort(
  dependencies: SyllabusWorkerDependencies,
  job: SyllabusWorkerJob,
  lease: Lease,
): Promise<void> {
  try {
    await dependencies.jobs.cleanupSource(job.id, lease);
  } catch { /* cleanup store persists a retryable pending record */ }
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
  await dependencies.jobs.finalizeFailure(
    job.id,
    lease,
    code,
    "The AI job did not complete",
    status,
    providerReconciled,
  );
  await emitTerminalTelemetry(dependencies, job, status, usage);
}

function validationOptions(
  dependencies: SyllabusWorkerDependencies,
  job: SyllabusWorkerJob,
): ProposalValidationOptions {
  const legacyAcceptedJob = job.targetTitle === null && job.openaiResponseId !== null;
  return {
    ...dependencies.validationLimits,
    expected: {
      schemaVersion: legacyAcceptedJob ? LEGACY_AI_SYLLABUS_PROPOSAL_SCHEMA_VERSION : AI_SYLLABUS_PROPOSAL_SCHEMA_VERSION,
      promptVersion: legacyAcceptedJob ? LEGACY_SYLLABUS_PROMPT_VERSION : SYLLABUS_PROMPT_VERSION,
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
      const cancellation = await providerCall(
        dependencies,
        job,
        "cancel",
        () => dependencies.provider.cancel(response.id),
      );
      await dependencies.jobs.assertLease(job.id, lease);
      if (terminalProviderStatus(cancellation.status)) {
        await finalizeFailure(
          dependencies,
          job,
          lease,
          "PROCESSING_DEADLINE_EXCEEDED",
          "EXPIRED",
          false,
          cancellation.usage,
        );
      } else await dependencies.jobs.reconcileProvider(job.id, lease, true);
      return;
    }
    await finalizeFailure(
      dependencies,
      job,
      lease,
      "PROCESSING_DEADLINE_EXCEEDED",
      "EXPIRED",
      false,
      response.usage,
    );
    return;
  }
  if (response.status === "queued" || response.status === "in_progress") {
    await dependencies.jobs.reconcileProvider(job.id, lease, true);
    // Polling an already accepted response is not a new provider generation.
    // Always release the lease so the scheduled worker can check it next cycle.
    await dependencies.jobs.markRetry(job.id, lease);
    return;
  }
  if (terminalProviderStatus(response.status)) {
    logProviderTerminalStatus(dependencies, job, response);
    await finalizeFailure(
      dependencies,
      job,
      lease,
      response.status === "incomplete"
        ? "PROVIDER_INCOMPLETE"
        : "PROVIDER_RESULT_UNAVAILABLE",
      "FAILED",
      false,
      response.usage,
    );
    return;
  }

  let proposal: AiSyllabusProposal;
  try {
    proposal = await validateAiSyllabusProposal(
      response.outputText ?? "",
      validationOptions(dependencies, job),
    );
  } catch (error) {
    if (error instanceof ProposalValidationError) {
      for (const code of error.diagnosticCodes) {
        const event: AiProviderDiagnostic = {
          event: "ai_provider_diagnostic",
          jobId: job.id,
          stage: "validation",
          outcome: "VALIDATION_REJECTED",
          code,
          message:
            "Structured proposal versions did not match the effective worker configuration",
        };
        try {
          if (dependencies.providerDiagnostics) {
            dependencies.providerDiagnostics(event);
          } else console.error(JSON.stringify(event));
        } catch {
          // Diagnostic delivery must not affect terminal job handling.
        }
      }
    }
    const code =
      error instanceof Error && error.message.startsWith("EMPTY_OUTPUT")
        ? "EMPTY_OUTPUT"
        : error instanceof Error &&
            error.message.startsWith("OUTPUT_LIMIT_EXCEEDED")
        ? "OUTPUT_LIMIT_EXCEEDED"
        : error instanceof Error && error.message.startsWith("VERSION_MISMATCH")
        ? "VERSION_MISMATCH"
        : "SCHEMA_MISMATCH";
    await finalizeFailure(
      dependencies,
      job,
      lease,
      code,
      "FAILED",
      false,
      response.usage,
    );
    return;
  }
  if (proposal.schemaVersion === AI_SYLLABUS_PROPOSAL_SCHEMA_VERSION && proposal.targetMatch !== "MATCHED") {
    await dependencies.jobs.captureUsage(job.id, lease, response.usage);
    await finalizeFailure(
      dependencies,
      job,
      lease,
      proposal.targetMatch === "NOT_FOUND" ? "TARGET_NOT_FOUND" : "TARGET_AMBIGUOUS",
      "FAILED",
      false,
      response.usage,
    );
    return;
  }
  await dependencies.jobs.captureUsage(job.id, lease, response.usage);
  await cleanupBestEffort(dependencies, job, lease);
  await dependencies.jobs.finalizeSuccess(
    job.id,
    lease,
    proposal,
    proposal.warnings,
    response.id,
  );
  await emitTerminalTelemetry(
    dependencies,
    job,
    "SUCCEEDED",
    response.usage,
    proposal,
  );
}

async function processReconciliation(
  dependencies: SyllabusWorkerDependencies,
  job: SyllabusWorkerJob,
  now: () => Date,
): Promise<void> {
  const lease = leaseOf(job);
  try {
    if (!job.openaiResponseId || !job.cancellationRequestedAt) {
      throw new Error("AI_RECONCILIATION_DATA_UNAVAILABLE");
    }
    await dependencies.jobs.assertLease(job.id, lease);
    let response = await providerCall(
      dependencies,
      job,
      "retrieve",
      () => dependencies.provider.retrieve(job.openaiResponseId!),
    );
    if (response.id !== job.openaiResponseId) {
      throw new Error("AI_PROVIDER_RESPONSE_ID_MISMATCH");
    }
    await dependencies.jobs.assertLease(job.id, lease);
    if (response.status === "queued" || response.status === "in_progress") {
      response = await providerCall(
        dependencies,
        job,
        "cancel",
        () => dependencies.provider.cancel(job.openaiResponseId!),
      );
      if (response.id !== job.openaiResponseId) {
        throw new Error("AI_PROVIDER_RESPONSE_ID_MISMATCH");
      }
      await dependencies.jobs.assertLease(job.id, lease);
    }
    if (response.status === "queued" || response.status === "in_progress") {
      await dependencies.jobs.failReconciliation(
        job.id,
        lease,
        "PROVIDER_CANCELLATION_PENDING",
      );
      return;
    }
    if (response.status === "completed") {
      await processResponse(
        dependencies,
        { ...job, processingDeadlineAt: null },
        lease,
        response,
        now(),
      );
    } else {
      const terminalStatus = response.status === "cancelled"
        ? "CANCELLED"
        : response.status === "expired"
        ? "EXPIRED"
        : "FAILED";
      const code = response.status === "incomplete"
        ? "PROVIDER_INCOMPLETE"
        : "PROVIDER_RESULT_UNAVAILABLE";
      await finalizeFailure(
        dependencies,
        job,
        lease,
        code,
        terminalStatus,
        false,
        response.usage,
      );
    }
    await dependencies.jobs.completeReconciliation(job.id, lease);
  } catch (error) {
    if (error instanceof LeaseLostError) return;
    try {
      await dependencies.jobs.failReconciliation(
        job.id,
        lease,
        errorCode(error),
      );
    } catch (leaseError) {
      if (!(leaseError instanceof LeaseLostError)) throw leaseError;
    }
  }
}

export async function processSyllabusJob(
  dependencies: SyllabusWorkerDependencies,
): Promise<boolean> {
  const now = dependencies.now ?? (() => new Date());
  const reconciliation = await dependencies.jobs.claimReconciliation(
    now(),
    dependencies.leaseSeconds ?? 300,
    dependencies.maxProcessingSeconds ?? 900,
  );
  if (reconciliation) {
    await processReconciliation(dependencies, reconciliation, now);
    return true;
  }
  const job = await dependencies.jobs.claimNext(
    now(),
    dependencies.leaseSeconds ?? 300,
    dependencies.maxProcessingSeconds ?? 900,
  );
  if (!job) return false;
  const lease = leaseOf(job);
  let providerStarted = job.openaiResponseId !== null ||
    job.providerQuarantinedAt != null ||
    (job.providerStartOutcome !== undefined &&
      job.providerStartOutcome !== "NOT_STARTED");
  try {
    await dependencies.jobs.assertLease(job.id, lease);
    if (
      job.cancellationRequestedAt != null || job.providerQuarantinedAt != null
    ) return true;
    if (deadlineExceeded(job, now()) && !providerStarted) {
      await finalizeFailure(
        dependencies,
        job,
        lease,
        "PROCESSING_DEADLINE_EXCEEDED",
        "EXPIRED",
        false,
      );
      return true;
    }
    let response: ProviderResponse;
    if (job.openaiResponseId !== null) {
      response = await providerCall(
        dependencies,
        job,
        "retrieve",
        () => dependencies.provider.retrieve(job.openaiResponseId!),
      );
      await dependencies.jobs.assertLease(job.id, lease);
    } else {
      if (job.targetTitle === null || genericTargetTitle(job.targetTitle)) {
        await finalizeFailure(dependencies, job, lease, "TARGET_AMBIGUOUS", "FAILED", false);
        return true;
      }
      const bytes = await dependencies.source(job);
      await dependencies.jobs.assertLease(job.id, lease);
      if (deadlineExceeded(job, now())) {
        await finalizeFailure(
          dependencies,
          job,
          lease,
          "PROCESSING_DEADLINE_EXCEEDED",
          "EXPIRED",
          false,
        );
        return true;
      }
      if (dependencies.jobs.markProviderStarted) {
        await dependencies.jobs.markProviderStarted(job.id, lease);
      }
      const providerStartedAt = now().toISOString();
      providerStarted = true;
      job.providerStartOutcome = "IN_FLIGHT";
      job.providerQuarantinedAt = providerStartedAt;
      job.providerExecutionStartedAt = providerStartedAt;
      try {
        response = await providerCall(
          dependencies,
          job,
          "start",
          () =>
            dependencies.provider.start({
              jobId: job.id,
              idempotencyKey: job.id,
              source: {
                filename: job.sourceObjectPath.split("/").pop() ?? "source.pdf",
                bytes,
              },
              systemPrompt: job.targetTitle === null ? LEGACY_SYLLABUS_SYSTEM_PROMPT : SYLLABUS_SYSTEM_PROMPT,
              userPrompt: job.targetTitle === null ? legacySyllabusUserPrompt() : syllabusUserPrompt(job.targetTitle),
              promptVersion: job.targetTitle === null ? LEGACY_SYLLABUS_PROMPT_VERSION : SYLLABUS_PROMPT_VERSION,
              schemaVersion: job.targetTitle === null ? LEGACY_AI_SYLLABUS_PROPOSAL_SCHEMA_VERSION : AI_SYLLABUS_PROPOSAL_SCHEMA_VERSION,
              schema: job.targetTitle === null ? LEGACY_AI_SYLLABUS_PROPOSAL_SCHEMA : AI_SYLLABUS_PROPOSAL_SCHEMA,
              model: resolveOpenAiModel(dependencies.model),
              reasoningEffort: "low",
              background: true,
              store: true,
              maxOutputTokens: dependencies.maxOutputTokens,
            }),
        );
      } catch (error) {
        if (!(error instanceof OpenAiProviderError) || !error.responseId) {
          throw error;
        }
        if (
          !await persistAcceptedResponseId(
            dependencies,
            job,
            lease,
            error.responseId,
          )
        ) return true;
        response = await providerCall(
          dependencies,
          job,
          "retrieve",
          () => dependencies.provider.retrieve(error.responseId!),
        );
        if (response.id !== error.responseId) {
          throw new Error("AI_PROVIDER_RESPONSE_ID_MISMATCH");
        }
        await dependencies.jobs.assertLease(job.id, lease);
      }
      if (
        job.openaiResponseId === null &&
        !await persistAcceptedResponseId(dependencies, job, lease, response.id)
      ) return true;
    }
    await processResponse(dependencies, job, lease, response, now());
  } catch (error) {
    if (
      error instanceof CancellationPendingError ||
      (error instanceof Error &&
        error.message === "AI_JOB_CANCELLATION_PENDING")
    ) return true;
    if (
      providerStarted && error instanceof OpenAiProviderError &&
      error.outcome === "NOT_SENT"
    ) {
      if (!dependencies.jobs.finalizeNotSent) {
        throw new Error("AI_WORKER_DATA_UNAVAILABLE");
      }
      try {
        await dependencies.jobs.finalizeNotSent(
          job.id,
          lease,
          errorCode(error),
        );
      } catch (notSentError) {
        if (!(notSentError instanceof LeaseLostError)) throw notSentError;
      }
      return true;
    }
    if (
      providerStarted && error instanceof OpenAiProviderError &&
      job.openaiResponseId === null
    ) {
      if (error.outcome !== "NOT_SENT") {
        try {
          await dependencies.jobs.recordProviderStartOutcome(
            job.id,
            lease,
            error.outcome,
          );
        } catch {
          /* retain IN_FLIGHT quarantine if classification cannot be persisted */
        }
      }
      return true;
    }
    if (error instanceof LeaseLostError) return true;
    const code = errorCode(error);
    if (preProviderDefinitive(code, providerStarted, job)) {
      await finalizeFailure(dependencies, job, lease, code, "FAILED", false);
      return true;
    }
    if (providerStarted && job.openaiResponseId === null) return true;
    try {
      await dependencies.jobs.assertLease(job.id, lease);
      await dependencies.jobs.reconcileProvider(job.id, lease, true);
      if (job.retryCount < (dependencies.maxRetries ?? 3)) {
        await dependencies.jobs.markRetry(job.id, lease);
      }
    } catch (leaseError) {
      if (!(leaseError instanceof LeaseLostError)) throw leaseError;
    }
  }
  return true;
}

export async function runSyllabusWorker(
  dependencies: SyllabusWorkerDependencies,
  maxJobs = 1,
): Promise<number> {
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
  serviceRoleJwt?: string;
  endpointAuthToken?: string;
  fetcher?: typeof fetch;
}

export function workerBackendHeaders(
  environment: Pick<
    WorkerRuntimeEnvironment,
    "serviceRoleKey" | "serviceRoleJwt"
  >,
): HeadersInit {
  const jwt = environment.serviceRoleJwt?.trim();
  if (jwt) {
    return {
      apikey: jwt,
      authorization: `Bearer ${jwt}`,
      accept: "application/json",
    };
  }
  if (environment.serviceRoleKey.startsWith("sb_secret_")) {
    return { apikey: environment.serviceRoleKey, accept: "application/json" };
  }
  return {
    apikey: environment.serviceRoleKey,
    authorization: `Bearer ${environment.serviceRoleKey}`,
    accept: "application/json",
  };
}

export function authorizeWorkerRequest(
  request: Request,
  expectedToken: string | undefined,
): boolean {
  const bearer = request.headers.get("authorization")?.replace(
    /^Bearer\s+/i,
    "",
  ).trim();
  return Boolean(expectedToken && bearer && bearer === expectedToken);
}

function row(value: unknown): Record<string, unknown> {
  if (
    Array.isArray(value) && value.length > 0 && typeof value[0] === "object" &&
    value[0] !== null
  ) return value[0] as Record<string, unknown>;
  if (typeof value === "object" && value !== null && !Array.isArray(value)) {
    return value as Record<string, unknown>;
  }
  throw new Error("AI_WORKER_DATA_UNAVAILABLE");
}

function noCompositeRow(value: unknown): boolean {
  if (value === null) return true;
  if (Array.isArray(value)) {
    if (value.length === 0 || value.length !== 1) return false;
    return noCompositeRow(value[0]);
  }
  if (typeof value !== "object" || value === null) return false;
  const fields = Object.values(value as Record<string, unknown>);
  return fields.length === 0 || fields.every((field) => field === null);
}

function stringField(value: Record<string, unknown>, key: string): string {
  if (typeof value[key] !== "string" || value[key].trim().length === 0) {
    throw new Error("AI_WORKER_DATA_UNAVAILABLE");
  }
  return value[key] as string;
}

function nullableString(
  value: Record<string, unknown>,
  key: string,
): string | null {
  return value[key] === null || value[key] === undefined
    ? null
    : stringField(value, key);
}

function integerField(value: Record<string, unknown>, key: string): number {
  if (typeof value[key] !== "number" || !Number.isSafeInteger(value[key])) {
    throw new Error("AI_WORKER_DATA_UNAVAILABLE");
  }
  return value[key] as number;
}

function parseJob(value: Record<string, unknown>): SyllabusWorkerJob {
  return {
    id: stringField(value, "id"),
    userId: stringField(value, "user_id"),
    status: "PROCESSING",
    sourceObjectPath: stringField(value, "source_object_path"),
    sourceHash: stringField(value, "source_hash"),
    sourceBytes: integerField(value, "source_bytes"),
    sourcePages: integerField(value, "source_pages"),
    sourceFileCount: integerField(value, "source_file_count"),
    targetTitle: parseTargetTitle(value.request_payload),
    openaiResponseId: nullableString(value, "openai_response_id"),
    providerExecutionStartedAt: nullableString(
      value,
      "provider_execution_started_at",
    ),
    providerStartOutcome: stringField(value, "provider_start_outcome"),
    providerQuarantinedAt: nullableString(value, "provider_quarantined_at"),
    cancellationRequestedAt: nullableString(value, "cancellation_requested_at"),
    leaseExpiresAt: nullableString(value, "lease_expires_at"),
    leaseOwner: stringField(value, "lease_owner"),
    leaseToken: stringField(value, "lease_token"),
    leaseGeneration: integerField(value, "lease_generation"),
    processingDeadlineAt: nullableString(value, "processing_deadline_at"),
    retryCount: integerField(value, "retry_count"),
    promptVersion: nullableString(value, "prompt_version"),
    modelVersion: nullableString(value, "model_version"),
  };
}

export function parseTargetTitle(requestPayload: unknown): string | null {
  if (typeof requestPayload !== "object" || requestPayload === null || Array.isArray(requestPayload)) return null;
  const target = (requestPayload as Record<string, unknown>).target;
  if (typeof target !== "object" || target === null || Array.isArray(target)) return null;
  const title = (target as Record<string, unknown>).title;
  return typeof title === "string" && title.trim().length > 0 ? title : null;
}

function genericTargetTitle(value: string): boolean {
  const normalized = value.normalize("NFD").replace(/[\u0300-\u036f]/g, "")
    .toLocaleLowerCase("pt-BR").trim();
  return /^(meu\s+)?edital(?:\s+(selecionado|importado))?$/.test(normalized) ||
    /^(concurso|prova|estudos?)$/.test(normalized);
}

export class SupabaseSyllabusWorkerStore implements SyllabusWorkerStore {
  private readonly fetcher: typeof fetch;
  private readonly workerOwner: string;
  constructor(
    private readonly environment: WorkerRuntimeEnvironment,
    private readonly storage: StorageSourceStore,
    workerOwner = `worker:${crypto.randomUUID()}`,
  ) {
    this.fetcher = environment.fetcher ?? fetch;
    this.workerOwner = workerOwner;
  }
  async claimNext(
    _now: Date,
    leaseSeconds: number,
    processingSeconds: number,
  ): Promise<SyllabusWorkerJob | null> {
    const value = await this.rpc("claim_ai_syllabus_worker_job", {
      p_lease_owner: this.workerOwner,
      p_lease_token: crypto.randomUUID(),
      p_lease_seconds: leaseSeconds,
      p_processing_seconds: processingSeconds,
    });
    if (noCompositeRow(value)) return null;
    return parseJob(row(value));
  }
  async claimReconciliation(
    _now: Date,
    leaseSeconds: number,
    reconciliationSeconds: number,
  ): Promise<SyllabusWorkerJob | null> {
    const value = await this.rpc("claim_ai_job_provider_reconciliation", {
      p_lease_owner: this.workerOwner,
      p_lease_token: crypto.randomUUID(),
      p_lease_seconds: leaseSeconds,
      p_reconciliation_seconds: reconciliationSeconds,
    });
    if (noCompositeRow(value)) return null;
    return parseJob(row(value));
  }
  async completeReconciliation(jobId: string, lease: Lease): Promise<void> {
    await this.rpc("complete_ai_job_provider_reconciliation", {
      p_job_id: jobId,
      ...leaseRpcArgs(lease),
    });
  }
  async failReconciliation(
    jobId: string,
    lease: Lease,
    code: string,
  ): Promise<void> {
    await this.rpc("fail_ai_job_provider_reconciliation", {
      p_job_id: jobId,
      p_error_code: code,
      ...leaseRpcArgs(lease),
    });
  }
  async assertLease(jobId: string, lease: Lease): Promise<void> {
    await this.rpc("assert_ai_job_lease", {
      p_job_id: jobId,
      ...leaseRpcArgs(lease),
    });
  }
  async markProviderStarted(jobId: string, lease: Lease): Promise<void> {
    await this.rpc("mark_ai_job_provider_execution_started", {
      p_job_id: jobId,
      ...leaseRpcArgs(lease),
    });
  }
  async recordProviderStartOutcome(
    jobId: string,
    lease: Lease,
    outcome: "PROVIDER_REJECTED" | "TRANSPORT_AMBIGUOUS" | "RESPONSE_AMBIGUOUS",
  ): Promise<void> {
    await this.rpc("record_ai_job_provider_start_outcome", {
      p_job_id: jobId,
      p_outcome: outcome,
      ...leaseRpcArgs(lease),
    });
  }
  async persistResponseId(
    jobId: string,
    responseId: string,
    lease: Lease,
  ): Promise<void> {
    await this.rpc("persist_ai_job_provider_response", {
      p_job_id: jobId,
      p_response_id: responseId,
      ...leaseRpcArgs(lease),
    });
  }
  async recoverResponseId(jobId: string, responseId: string): Promise<void> {
    await this.rpc("recover_ai_job_provider_response", {
      p_job_id: jobId,
      p_response_id: responseId,
    });
  }
  async finalizeNotSent(
    jobId: string,
    lease: Lease,
    code: string,
  ): Promise<void> {
    await this.rpc("finalize_ai_job_not_sent", {
      p_job_id: jobId,
      p_error_code: code,
      ...leaseRpcArgs(lease),
    });
  }
  async reconcileProvider(
    jobId: string,
    lease: Lease,
    recoverable: boolean,
  ): Promise<void> {
    await this.rpc("record_ai_job_provider_reconciliation", {
      p_job_id: jobId,
      p_recoverable: recoverable,
      ...leaseRpcArgs(lease),
    });
  }
  async markRetry(jobId: string, lease: Lease): Promise<void> {
    await this.rpc("increment_ai_job_retry", {
      p_job_id: jobId,
      ...leaseRpcArgs(lease),
    });
  }
  async captureUsage(
    jobId: string,
    lease: Lease,
    usage: ProviderUsage | null,
  ): Promise<void> {
    await this.rpc("record_ai_job_usage", {
      p_job_id: jobId,
      p_input_tokens: usage?.inputTokens ?? null,
      p_output_tokens: usage?.outputTokens ?? null,
      p_total_tokens: usage?.totalTokens ?? null,
      ...leaseRpcArgs(lease),
    });
  }
  async finalizeSuccess(
    jobId: string,
    lease: Lease,
    proposal: AiSyllabusProposal,
    warnings: AiWarning[],
    responseId: string | null,
  ): Promise<void> {
    await this.rpc("finalize_ai_job_success_with_lease", {
      p_job_id: jobId,
      p_proposal: proposal,
      p_warnings: warnings,
      p_openai_response_id: responseId,
      p_prompt_version: proposal.promptVersion,
      p_schema_version: proposal.schemaVersion,
      p_model_version: proposal.modelVersion,
      ...leaseRpcArgs(lease),
    });
  }
  async finalizeFailure(
    jobId: string,
    lease: Lease,
    code: string,
    message: string,
    terminalStatus: "FAILED" | "EXPIRED" | "CANCELLED",
    providerReconciled: boolean,
  ): Promise<void> {
    await this.rpc("finalize_ai_job_failure_with_lease", {
      p_job_id: jobId,
      p_terminal_status: terminalStatus,
      p_error_code: code,
      p_error_message: message,
      p_provider_reconciled: providerReconciled,
      ...leaseRpcArgs(lease),
    });
  }
  async cleanupSource(jobId: string, lease: Lease): Promise<void> {
    const cleanup = row(
      await this.rpc("prepare_ai_job_source_cleanup", {
        p_job_id: jobId,
        ...leaseRpcArgs(lease),
      }),
    );
    if (cleanup.status === "DELETED") return;
    const path = stringField(cleanup, "source_object_path");
    try {
      const response = await this.fetcher(
        `${
          this.environment.supabaseUrl.replace(/\/$/, "")
        }/storage/v1/object/${AI_SYLLABUS_SOURCE_BUCKET}/${path}`,
        { method: "DELETE", headers: workerBackendHeaders(this.environment) },
      );
      if (!response.ok && response.status !== 404) {
        throw new Error("AI_SOURCE_CLEANUP_FAILED");
      }
      await this.rpc("complete_ai_job_source_cleanup", {
        p_job_id: jobId,
        ...leaseRpcArgs(lease),
      });
    } catch (error) {
      await this.rpc("fail_ai_job_source_cleanup", {
        p_job_id: jobId,
        p_error: error instanceof Error
          ? error.message
          : "AI_SOURCE_CLEANUP_FAILED",
        ...leaseRpcArgs(lease),
      });
      throw error;
    }
  }
  async cleanupPendingSources(limit = 10): Promise<void> {
    for (let index = 0; index < limit; index += 1) {
      const owner = `${this.workerOwner}:cleanup`, token = crypto.randomUUID();
      const value = await this.rpc("claim_ai_job_source_cleanup", {
        p_lease_owner: owner,
        p_lease_token: token,
        p_lease_seconds: 300,
      });
      if (noCompositeRow(value)) return;
      const cleanup = row(value),
        jobId = stringField(cleanup, "job_id"),
        path = stringField(cleanup, "source_object_path");
      const cleanupLease = leaseRpcArgs({
        owner,
        token,
        generation: integerField(cleanup, "lease_generation"),
      });
      try {
        const response = await this.fetcher(
          `${
            this.environment.supabaseUrl.replace(/\/$/, "")
          }/storage/v1/object/${AI_SYLLABUS_SOURCE_BUCKET}/${path}`,
          { method: "DELETE", headers: workerBackendHeaders(this.environment) },
        );
        if (!response.ok && response.status !== 404) {
          throw new Error("AI_SOURCE_CLEANUP_FAILED");
        }
        await this.rpc("complete_ai_job_source_cleanup", {
          p_job_id: jobId,
          ...cleanupLease,
        });
      } catch (error) {
        await this.rpc("fail_ai_job_source_cleanup", {
          p_job_id: jobId,
          p_error: error instanceof Error
            ? error.message
            : "AI_SOURCE_CLEANUP_FAILED",
          ...cleanupLease,
        });
      }
    }
  }
  private async rpc(
    name: string,
    body: Record<string, unknown>,
  ): Promise<unknown> {
    const response = await this.fetcher(
      `${this.environment.supabaseUrl.replace(/\/$/, "")}/rest/v1/rpc/${name}`,
      {
        method: "POST",
        headers: {
          ...workerBackendHeaders(this.environment),
          "content-type": "application/json",
        },
        body: JSON.stringify(body),
      },
    );
    if (!response.ok) {
      let providerMessage = "";
      try {
        const payload = await response.clone().json() as Record<
          string,
          unknown
        >;
        providerMessage = [payload.code, payload.message, payload.error].filter(
          (value) => typeof value === "string",
        ).join(" ");
      } catch { /* retain the safe generic error */ }
      if (
        response.status === 409 || response.status === 412 ||
        /AI_JOB_LEASE_LOST|LEASE_LOST/.test(providerMessage)
      ) throw new LeaseLostError();
      if (providerMessage.includes("AI_JOB_CANCELLATION_PENDING")) {
        throw new CancellationPendingError();
      }
      throw new Error("AI_WORKER_DATA_UNAVAILABLE");
    }
    return await response.json();
  }
}

function environmentNumber(name: string, fallback: number): number {
  const value = Number(Deno.env.get(name));
  return Number.isSafeInteger(value) && value > 0 ? value : fallback;
}
function runtimeEnvironment(): WorkerRuntimeEnvironment {
  const supabaseUrl = Deno.env.get("SUPABASE_URL")?.trim(),
    serviceRoleKey = Deno.env.get("SUPABASE_SERVICE_ROLE_KEY")?.trim();
  const serviceRoleJwt = Deno.env.get("AI_SERVICE_ROLE_JWT")?.trim();
  const endpointAuthToken = Deno.env.get("AI_WORKER_AUTH_TOKEN")?.trim();
  if (!supabaseUrl || !serviceRoleKey || !endpointAuthToken) {
    throw new Error("AI_WORKER_NOT_CONFIGURED");
  }
  return { supabaseUrl, serviceRoleKey, serviceRoleJwt, endpointAuthToken };
}
function runtimeDependencies(): SyllabusWorkerDependencies & {
  runtimeStore: SupabaseSyllabusWorkerStore;
} {
  const environment = runtimeEnvironment();
  const backendKey = environment.serviceRoleJwt ?? environment.serviceRoleKey;
  const storage = new SupabaseStorageSourceStore({
    supabaseUrl: environment.supabaseUrl,
    publishableKey: backendKey,
    accessToken: backendKey,
    serviceRoleKey: environment.serviceRoleKey,
    serviceRoleJwt: environment.serviceRoleJwt,
  }, environmentNumber("MAX_PDF_BYTES", 50 * 1024 * 1024));
  const runtimeStore = new SupabaseSyllabusWorkerStore(environment, storage);
  return {
    runtimeStore,
    jobs: runtimeStore,
    provider: createOpenAiProvider({
      background: true,
      model: resolveOpenAiModel(),
      maxOutputTokens: environmentNumber("MAX_OUTPUT_TOKENS", 16_384),
      timeoutMs: environmentNumber("OPENAI_TIMEOUT_MS", 30_000),
      store: true,
    }),
    source: async (job) => {
      const object = await storage.getObject(job.userId, job.sourceObjectPath);
      if (!object) throw new Error("SOURCE_NOT_FOUND");
      const buffer = new ArrayBuffer(object.body.byteLength);
      new Uint8Array(buffer).set(object.body);
      const digest = await crypto.subtle.digest("SHA-256", buffer);
      const hash = [...new Uint8Array(digest)].map((byte) =>
        byte.toString(16).padStart(2, "0")
      ).join("");
      if (hash !== job.sourceHash) throw new Error("SOURCE_HASH_MISMATCH");
      return object.body;
    },
    leaseSeconds: environmentNumber("AI_WORKER_LEASE_SECONDS", 300),
    maxRetries: environmentNumber("AI_MAX_RETRIES", 3),
    maxOutputTokens: environmentNumber("MAX_OUTPUT_TOKENS", 16_384),
    maxProcessingSeconds: environmentNumber("MAX_PROCESSING_SECONDS", 1800),
    model: resolveOpenAiModel(),
    validationLimits: {
      maxSubjects: environmentNumber("MAX_SUBJECTS", 100),
      maxTopics: environmentNumber("MAX_TOPICS", 2000),
      maxTopicDepth: environmentNumber("MAX_TOPIC_DEPTH", 8),
    },
  };
}

if (import.meta.main) {
  Deno.serve(async (request) => {
    if (request.method !== "POST") {
      return new Response(null, { status: 405, headers: { allow: "POST" } });
    }
    try {
      const environment = runtimeEnvironment();
      if (!authorizeWorkerRequest(request, environment.endpointAuthToken)) {
        return Response.json({
          error: {
            code: "AI_WORKER_UNAUTHORIZED",
            message: "AI worker authorization required",
          },
        }, { status: 401 });
      }
      const dependencies = runtimeDependencies();
      await dependencies.runtimeStore.cleanupPendingSources();
      const processed = await runSyllabusWorker(
        dependencies,
        environmentNumber("AI_WORKER_BATCH_SIZE", 1),
      );
      return Response.json({ processed }, {
        headers: { "cache-control": "no-store" },
      });
    } catch {
      return Response.json({
        error: {
          code: "AI_WORKER_UNAVAILABLE",
          message: "AI worker is unavailable",
        },
      }, { status: 503 });
    }
  });
}
