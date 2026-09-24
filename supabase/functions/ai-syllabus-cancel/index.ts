import { authenticateSupabaseRequest, AuthError, type AuthenticatedUser } from "../_shared/auth.ts";
import { JobStoreError, SupabaseAiJobStore, type AiJobRecord, type AiJobCancellationStore } from "../_shared/job-finalizer.ts";
import {
  createOpenAiProvider,
  resolveOpenAiModel,
  type OpenAiProvider,
  type ProviderResponse,
} from "../_shared/openai-provider.ts";
import { validateAiSyllabusProposal } from "../_shared/proposal-validator.ts";
import { SYLLABUS_PROMPT_VERSION } from "../_shared/prompts/syllabus-v1.ts";
import { AI_SYLLABUS_PROPOSAL_SCHEMA_VERSION } from "../_shared/schemas/ai-syllabus-proposal-v1.ts";

export interface AiSyllabusCancelDependencies {
  authenticate: (request: Request) => Promise<AuthenticatedUser>;
  jobs: AiJobCancellationStore;
  provider: OpenAiProvider;
}

function jsonResponse(body: unknown, status = 200): Response {
  return new Response(JSON.stringify(body), {
    status,
    headers: { "content-type": "application/json; charset=utf-8", "cache-control": "no-store" },
  });
}

function safeError(code: string, status: number): Response {
  const messages: Record<string, string> = {
    AUTH_REQUIRED: "Authentication required",
    AUTH_INVALID: "Authentication required",
    AI_JOB_NOT_FOUND: "AI job not found",
    CANCELLATION_RECONCILIATION_REQUIRED: "The provider must be reconciled before this job can be cancelled",
    AI_JOB_NOT_CANCELLABLE: "The AI job cannot be cancelled",
  };
  return jsonResponse({ error: { code, message: messages[code] ?? "AI job cancellation could not be completed" } }, status);
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

async function completedProposal(response: ProviderResponse): Promise<Record<string, unknown>> {
  const proposal = await validateAiSyllabusProposal(response.outputText ?? "", {
    expected: {
      schemaVersion: AI_SYLLABUS_PROPOSAL_SCHEMA_VERSION,
      promptVersion: SYLLABUS_PROMPT_VERSION,
      modelVersion: resolveOpenAiModel(),
    },
  });
  return proposal as unknown as Record<string, unknown>;
}

async function recordUnknown(
  dependencies: AiSyllabusCancelDependencies,
  userId: string,
  jobId: string,
  responseId: string | null,
  errorCode: string,
): Promise<Response> {
  const job = await dependencies.jobs.recordCancellationReconciliation(userId, jobId, {
    responseId,
    providerStatus: "unknown",
    resultRecoverable: true,
    errorCode,
  });
  return jobResponse(job, 202);
}

async function finalizeCompleted(
  dependencies: AiSyllabusCancelDependencies,
  userId: string,
  jobId: string,
  response: ProviderResponse,
): Promise<Response> {
  try {
    const proposal = await completedProposal(response);
    const job = await dependencies.jobs.finalizeCancellationSuccess(userId, jobId, response, proposal);
    return jobResponse(job);
  } catch (error) {
    if (error instanceof JobStoreError) throw error;
    const reconciled = await dependencies.jobs.recordCancellationReconciliation(userId, jobId, {
      responseId: response.id,
      providerStatus: "completed",
      resultRecoverable: false,
      errorCode: "PROVIDER_RESULT_INVALID",
    });
    if (terminal(reconciled.status)) return jobResponse(reconciled);
    const job = await dependencies.jobs.cancelAfterReconciliation(userId, jobId, "PROVIDER_RESULT_INVALID", "The provider result was not valid", "FAILED");
    return jobResponse(job);
  }
}

async function reconcileWithProvider(
  dependencies: AiSyllabusCancelDependencies,
  userId: string,
  job: AiJobRecord,
): Promise<Response> {
  const responseId = job.openaiResponseId;
  if (!responseId) return recordUnknown(dependencies, userId, job.id, null, "PROVIDER_RESPONSE_ID_MISSING");

  let response: ProviderResponse;
  try {
    response = await dependencies.provider.retrieve(responseId);
  } catch {
    return recordUnknown(dependencies, userId, job.id, responseId, "PROVIDER_RETRIEVE_UNKNOWN");
  }
  if (response.status === "completed") return finalizeCompleted(dependencies, userId, job.id, response);
  if (providerTerminal(response.status)) {
    const reconciled = await dependencies.jobs.recordCancellationReconciliation(userId, job.id, {
      responseId,
      providerStatus: response.status,
      resultRecoverable: false,
    });
    if (terminal(reconciled.status)) return jobResponse(reconciled);
    return jobResponse(await dependencies.jobs.cancelAfterReconciliation(userId, job.id, "PROVIDER_CANCELLED", "Provider confirmed that no result is recoverable"));
  }

  let cancellation: ProviderResponse;
  try {
    cancellation = await dependencies.provider.cancel(response.id);
  } catch {
    return recordUnknown(dependencies, userId, job.id, response.id, "PROVIDER_CANCEL_UNKNOWN");
  }
  if (cancellation.status === "completed") return finalizeCompleted(dependencies, userId, job.id, cancellation);
  if (providerTerminal(cancellation.status)) {
    const reconciled = await dependencies.jobs.recordCancellationReconciliation(userId, job.id, {
      responseId: cancellation.id,
      providerStatus: cancellation.status,
      resultRecoverable: false,
    });
    if (terminal(reconciled.status)) return jobResponse(reconciled);
    return jobResponse(await dependencies.jobs.cancelAfterReconciliation(userId, job.id, "PROVIDER_CANCELLED", "Provider confirmed cancellation without a result"));
  }
  return recordUnknown(dependencies, userId, job.id, cancellation.id, "PROVIDER_CANCEL_PENDING");
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

  if (!job.openaiResponseId && !job.providerExecutionStartedAt) {
    try {
      return jobResponse(await dependencies.jobs.cancelWithoutProvider(userId, jobId));
    } catch (error) {
      if (!(error instanceof JobStoreError) || error.code !== "CANCELLATION_RECONCILIATION_REQUIRED") throw error;
      job = await dependencies.jobs.getJob(userId, jobId) ?? job;
      if (terminal(job.status)) return jobResponse(job);
    }
  }

  if (!job.openaiResponseId) return recordUnknown(dependencies, userId, job.id, null, "PROVIDER_RESPONSE_ID_MISSING");
  return reconcileWithProvider(dependencies, userId, job);
}

export function createAiSyllabusCancelHandler(dependencies: AiSyllabusCancelDependencies): (request: Request) => Promise<Response> {
  return async (request: Request): Promise<Response> => {
    if (!request.headers.get("authorization")) return safeError("AUTH_REQUIRED", 401);
    if (request.method !== "POST") return jsonResponse({ error: { code: "METHOD_NOT_ALLOWED", message: "Only POST is supported" } }, 405);
    let user: AuthenticatedUser;
    try {
      user = await dependencies.authenticate(request);
    } catch (error) {
      if (error instanceof AuthError) return safeError(error.code, error.status);
      return safeError("AUTH_UNAVAILABLE", 503);
    }
    const match = new URL(request.url).pathname.replace(/\/+$/, "").match(/\/ai-syllabus\/jobs\/([^/]+)\/cancel$/);
    if (!match || !match[1]) return safeError("NOT_FOUND", 404);
    try {
      return await cancelJob(dependencies, user.userId, match[1]);
    } catch (error) {
      if (error instanceof JobStoreError) return safeError(error.code, error.status);
      return safeError("AI_JOB_UNAVAILABLE", 503);
    }
  };
}

function runtimeDependencies(request: Request): AiSyllabusCancelDependencies {
  const supabaseUrl = Deno.env.get("SUPABASE_URL")?.trim();
  const publishableKey = (Deno.env.get("SUPABASE_ANON_KEY") ?? Deno.env.get("SUPABASE_PUBLISHABLE_KEY"))?.trim();
  const serviceRoleKey = Deno.env.get("SUPABASE_SERVICE_ROLE_KEY")?.trim();
  if (!supabaseUrl || !publishableKey || !serviceRoleKey) throw new AuthError("AUTH_UNAVAILABLE", 503);
  const accessToken = request.headers.get("authorization")?.replace(/^Bearer\s+/i, "").trim() ?? "";
  const jobs = new SupabaseAiJobStore({ supabaseUrl, publishableKey, accessToken, serviceRoleKey });
  return { authenticate: authenticateSupabaseRequest, jobs, provider: createOpenAiProvider() };
}

async function handle(request: Request): Promise<Response> {
  try {
    return await createAiSyllabusCancelHandler(runtimeDependencies(request))(request);
  } catch (error) {
    if (error instanceof AuthError) return safeError(error.code, error.status);
    return safeError("AI_JOB_UNAVAILABLE", 503);
  }
}

if (import.meta.main) Deno.serve(handle);
