import {
  type AuthenticatedUser,
  authenticateSupabaseRequest,
  AuthError,
} from "../_shared/auth.ts";
import {
  type AiJobCancellationStore,
  type AiJobRecord,
  emitAiTerminalTelemetry,
  JobStoreError,
  SupabaseAiJobStore,
  type TerminalAiTelemetry,
} from "../_shared/job-finalizer.ts";
import {
  createOpenAiProvider,
  type OpenAiProvider,
  type ProviderResponse,
  resolveOpenAiModel,
} from "../_shared/openai-provider.ts";
import { validateAiSyllabusProposal } from "../_shared/proposal-validator.ts";
import { SYLLABUS_PROMPT_VERSION } from "../_shared/prompts/syllabus-v2.ts";
import { SYLLABUS_PROMPT_VERSION as LEGACY_SYLLABUS_PROMPT_VERSION } from "../_shared/prompts/syllabus-v1.ts";
import { AI_SYLLABUS_PROPOSAL_SCHEMA_VERSION } from "../_shared/schemas/ai-syllabus-proposal-v2.ts";
import { AI_SYLLABUS_PROPOSAL_SCHEMA_VERSION as LEGACY_AI_SYLLABUS_PROPOSAL_SCHEMA_VERSION } from "../_shared/schemas/ai-syllabus-proposal-v1.ts";

export interface AiSyllabusCancelDependencies {
  authenticate: (request: Request) => Promise<AuthenticatedUser>;
  jobs: AiJobCancellationStore;
  provider: OpenAiProvider;
  telemetry?: (event: TerminalAiTelemetry) => void;
}

async function reportFinalized(
  dependencies: AiSyllabusCancelDependencies,
  job: AiJobRecord,
  response?: ProviderResponse,
): Promise<void> {
  if (!terminal(job.status)) return;
  await emitAiTerminalTelemetry({
    feature: job.feature,
    userId: job.userId,
    jobId: job.id,
    modelVersion: job.modelVersion ?? resolveOpenAiModel(),
    promptVersion: job.promptVersion ?? SYLLABUS_PROMPT_VERSION,
    schemaVersion: job.schemaVersion ?? AI_SYLLABUS_PROPOSAL_SCHEMA_VERSION,
    usage: response?.usage,
    startedAt: job.providerExecutionStartedAt,
    terminalStatus: job.status as TerminalAiTelemetry["terminalStatus"],
    sink: dependencies.telemetry,
  });
}

function jsonResponse(body: unknown, status = 200): Response {
  return new Response(JSON.stringify(body), {
    status,
    headers: {
      "content-type": "application/json; charset=utf-8",
      "cache-control": "no-store",
    },
  });
}

function safeError(code: string, status: number): Response {
  const messages: Record<string, string> = {
    AUTH_REQUIRED: "Authentication required",
    AUTH_INVALID: "Authentication required",
    AI_JOB_NOT_FOUND: "AI job not found",
    CANCELLATION_RECONCILIATION_REQUIRED:
      "The provider must be reconciled before this job can be cancelled",
    AI_JOB_NOT_CANCELLABLE: "The AI job cannot be cancelled",
  };
  return jsonResponse({
    error: {
      code,
      message: messages[code] ?? "AI job cancellation could not be completed",
    },
  }, status);
}

function terminal(status: AiJobRecord["status"]): boolean {
  return ["SUCCEEDED", "FAILED", "EXPIRED", "CANCELLED"].includes(status);
}

function providerTerminal(status: ProviderResponse["status"]): boolean {
  return ["failed", "cancelled", "expired", "incomplete"].includes(status);
}

function jobResponse(job: AiJobRecord, status = 200): Response {
  return jsonResponse({
    jobId: job.id,
    status: job.status,
    providerReconciledAt: job.providerReconciledAt ?? null,
    providerResultRecoverable: job.providerResultRecoverable ?? null,
  }, status);
}

async function completedProposal(
  response: ProviderResponse,
  job: AiJobRecord,
): Promise<Record<string, unknown>> {
  const requestPayload = job.requestPayload;
  const target = Object.prototype.hasOwnProperty.call(requestPayload, "target")
    ? requestPayload.target
    : undefined;
  const title = typeof target === "object" && target !== null && !Array.isArray(target)
    ? (target as Record<string, unknown>).title
    : null;
  const legacyJob = target === undefined;
  if (!legacyJob && (typeof title !== "string" || title.trim().length === 0)) {
    throw new Error("TARGET_AMBIGUOUS");
  }
  const proposal = await validateAiSyllabusProposal(response.outputText ?? "", {
    expected: {
      schemaVersion: legacyJob ? LEGACY_AI_SYLLABUS_PROPOSAL_SCHEMA_VERSION : AI_SYLLABUS_PROPOSAL_SCHEMA_VERSION,
      promptVersion: legacyJob ? LEGACY_SYLLABUS_PROMPT_VERSION : SYLLABUS_PROMPT_VERSION,
      modelVersion: resolveOpenAiModel(),
    },
  });
  if (!legacyJob && proposal.targetMatch !== "MATCHED") {
    throw new Error(proposal.targetMatch === "NOT_FOUND" ? "TARGET_NOT_FOUND" : "TARGET_AMBIGUOUS");
  }
  return proposal as unknown as Record<string, unknown>;
}

async function recordUnknown(
  dependencies: AiSyllabusCancelDependencies,
  userId: string,
  jobId: string,
  responseId: string | null,
  errorCode: string,
): Promise<Response> {
  const job = await dependencies.jobs.recordCancellationReconciliation(
    userId,
    jobId,
    {
      responseId,
      providerStatus: "unknown",
      resultRecoverable: true,
      errorCode,
    },
  );
  return jobResponse(job, 202);
}

async function finalizeCompleted(
  dependencies: AiSyllabusCancelDependencies,
  userId: string,
  jobId: string,
  response: ProviderResponse,
): Promise<Response> {
  try {
    const currentJob = await dependencies.jobs.getJob(userId, jobId);
    if (!currentJob) return safeError("AI_JOB_NOT_FOUND", 404);
    const proposal = await completedProposal(response, currentJob);
    const finalizedJob = await dependencies.jobs.finalizeCancellationSuccess(
      userId,
      jobId,
      response,
      proposal,
    );
    await reportFinalized(dependencies, finalizedJob, response);
    return jobResponse(finalizedJob);
  } catch (error) {
    if (error instanceof JobStoreError) throw error;
    const targetCode = error instanceof Error && ["TARGET_NOT_FOUND", "TARGET_AMBIGUOUS"].includes(error.message)
      ? error.message
      : null;
    if (targetCode !== null) {
      await dependencies.jobs.recordCancellationReconciliation(userId, jobId, {
        responseId: response.id,
        providerStatus: "completed",
        resultRecoverable: false,
        errorCode: targetCode,
      });
      const job = await dependencies.jobs.cancelAfterReconciliation(
        userId,
        jobId,
        targetCode,
        "The selected role or area could not be matched to the source",
        "FAILED",
      );
      await reportFinalized(dependencies, job, response);
      return jobResponse(job);
    }
    const reconciled = await dependencies.jobs.recordCancellationReconciliation(
      userId,
      jobId,
      {
        responseId: response.id,
        providerStatus: "completed",
        resultRecoverable: false,
        errorCode: "PROVIDER_RESULT_INVALID",
      },
    );
    if (terminal(reconciled.status)) return jobResponse(reconciled);
    const job = await dependencies.jobs.cancelAfterReconciliation(
      userId,
      jobId,
      "PROVIDER_RESULT_INVALID",
      "The provider result was not valid",
      "FAILED",
    );
    await reportFinalized(dependencies, job, response);
    return jobResponse(job);
  }
}

async function reconcileWithProvider(
  dependencies: AiSyllabusCancelDependencies,
  userId: string,
  job: AiJobRecord,
): Promise<Response> {
  const responseId = job.openaiResponseId;
  if (!responseId) {
    return recordUnknown(
      dependencies,
      userId,
      job.id,
      null,
      "PROVIDER_RESPONSE_ID_MISSING",
    );
  }

  let response: ProviderResponse;
  try {
    response = await dependencies.provider.retrieve(responseId);
  } catch {
    return recordUnknown(
      dependencies,
      userId,
      job.id,
      responseId,
      "PROVIDER_RETRIEVE_UNKNOWN",
    );
  }
  if (response.status === "completed") {
    return finalizeCompleted(dependencies, userId, job.id, response);
  }
  if (providerTerminal(response.status)) {
    const reconciled = await dependencies.jobs.recordCancellationReconciliation(
      userId,
      job.id,
      {
        responseId,
        providerStatus: response.status,
        resultRecoverable: false,
      },
    );
    if (terminal(reconciled.status)) return jobResponse(reconciled);
    const finalized = await dependencies.jobs.cancelAfterReconciliation(
      userId,
      job.id,
      "PROVIDER_CANCELLED",
      "Provider confirmed that no result is recoverable",
    );
    await reportFinalized(dependencies, finalized, response);
    return jobResponse(finalized);
  }

  let cancellation: ProviderResponse;
  try {
    cancellation = await dependencies.provider.cancel(response.id);
  } catch {
    return recordUnknown(
      dependencies,
      userId,
      job.id,
      response.id,
      "PROVIDER_CANCEL_UNKNOWN",
    );
  }
  if (cancellation.status === "completed") {
    return finalizeCompleted(dependencies, userId, job.id, cancellation);
  }
  if (providerTerminal(cancellation.status)) {
    const reconciled = await dependencies.jobs.recordCancellationReconciliation(
      userId,
      job.id,
      {
        responseId: cancellation.id,
        providerStatus: cancellation.status,
        resultRecoverable: false,
      },
    );
    if (terminal(reconciled.status)) return jobResponse(reconciled);
    const finalized = await dependencies.jobs.cancelAfterReconciliation(
      userId,
      job.id,
      "PROVIDER_CANCELLED",
      "Provider confirmed cancellation without a result",
    );
    await reportFinalized(dependencies, finalized, cancellation);
    return jobResponse(finalized);
  }
  return recordUnknown(
    dependencies,
    userId,
    job.id,
    cancellation.id,
    "PROVIDER_CANCEL_PENDING",
  );
}

async function cancelJob(
  dependencies: AiSyllabusCancelDependencies,
  userId: string,
  jobId: string,
): Promise<Response> {
  let job = await dependencies.jobs.getJob(userId, jobId);
  if (!job) return safeError("AI_JOB_NOT_FOUND", 404);
  if (terminal(job.status)) return jobResponse(job);

  job = await dependencies.jobs.requestCancellation(userId, jobId);
  if (terminal(job.status)) return jobResponse(job);

  if (hasActiveGenerationLease(job.leaseExpiresAt)) {
    return jobResponse(job, 202);
  }

  if (!job.openaiResponseId) {
    if (
      job.providerStartOutcome !== "NOT_STARTED" ||
      job.providerQuarantinedAt != null
    ) return jobResponse(job, 202);

    try {
      const finalized = await dependencies.jobs.cancelWithoutProvider(
        userId,
        jobId,
      );
      await reportFinalized(dependencies, finalized);
      return jobResponse(finalized);
    } catch (error) {
      if (
        !(error instanceof JobStoreError) ||
        error.code !== "CANCELLATION_RECONCILIATION_REQUIRED"
      ) throw error;
      job = await dependencies.jobs.getJob(userId, jobId) ?? job;
      if (terminal(job.status)) return jobResponse(job);
    }
  }

  if (!job.openaiResponseId) return jobResponse(job, 202);
  return reconcileWithProvider(dependencies, userId, job);
}

function hasActiveGenerationLease(
  leaseExpiresAt: string | null | undefined,
): boolean {
  if (leaseExpiresAt == null) return false;
  const expiresAt = Date.parse(leaseExpiresAt);
  // Invalid lease data is treated conservatively as active; the database RPC
  // remains the final atomic guard before releasing quota.
  return !Number.isFinite(expiresAt) || expiresAt > Date.now();
}

const UUID_PATTERN =
  /^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$/i;

export function extractAiSyllabusCancelJobId(pathname: string): string | null {
  const withoutOneTrailingSlash = pathname.endsWith("/")
    ? pathname.slice(0, -1)
    : pathname;
  const functionPrefix = "/functions/v1";
  const routePath = withoutOneTrailingSlash.startsWith(
      `${functionPrefix}/ai-syllabus-cancel/`,
    )
    ? withoutOneTrailingSlash.slice(functionPrefix.length)
    : withoutOneTrailingSlash;
  const match = /^\/ai-syllabus-cancel\/([^/]+)$/.exec(routePath);
  if (!match || !UUID_PATTERN.test(match[1])) return null;
  return match[1];
}

export function createAiSyllabusCancelHandler(
  dependencies: AiSyllabusCancelDependencies,
): (request: Request) => Promise<Response> {
  return async (request: Request): Promise<Response> => {
    if (!request.headers.get("authorization")) {
      return safeError("AUTH_REQUIRED", 401);
    }
    if (request.method !== "POST") {
      return jsonResponse({
        error: {
          code: "METHOD_NOT_ALLOWED",
          message: "Only POST is supported",
        },
      }, 405);
    }
    let user: AuthenticatedUser;
    try {
      user = await dependencies.authenticate(request);
    } catch (error) {
      if (error instanceof AuthError) {
        return safeError(error.code, error.status);
      }
      return safeError("AUTH_UNAVAILABLE", 503);
    }
    const jobId = extractAiSyllabusCancelJobId(new URL(request.url).pathname);
    if (!jobId) return safeError("NOT_FOUND", 404);
    try {
      return await cancelJob(dependencies, user.userId, jobId);
    } catch (error) {
      if (error instanceof JobStoreError) {
        return safeError(error.code, error.status);
      }
      return safeError("AI_JOB_UNAVAILABLE", 503);
    }
  };
}

function runtimeDependencies(request: Request): AiSyllabusCancelDependencies {
  const supabaseUrl = Deno.env.get("SUPABASE_URL")?.trim();
  const publishableKey = (Deno.env.get("SUPABASE_ANON_KEY") ??
    Deno.env.get("SUPABASE_PUBLISHABLE_KEY"))?.trim();
  const serviceRoleKey = Deno.env.get("SUPABASE_SERVICE_ROLE_KEY")?.trim();
  if (!supabaseUrl || !publishableKey || !serviceRoleKey) {
    throw new AuthError("AUTH_UNAVAILABLE", 503);
  }
  const accessToken =
    request.headers.get("authorization")?.replace(/^Bearer\s+/i, "").trim() ??
      "";
  const jobs = new SupabaseAiJobStore({
    supabaseUrl,
    publishableKey,
    accessToken,
    serviceRoleKey,
  });
  return {
    authenticate: authenticateSupabaseRequest,
    jobs,
    provider: createOpenAiProvider(),
  };
}

async function handle(request: Request): Promise<Response> {
  try {
    return await createAiSyllabusCancelHandler(runtimeDependencies(request))(
      request,
    );
  } catch (error) {
    if (error instanceof AuthError) return safeError(error.code, error.status);
    return safeError("AI_JOB_UNAVAILABLE", 503);
  }
}

if (import.meta.main) Deno.serve(handle);
