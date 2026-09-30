import type { AiAccess, AiFeature } from "../_shared/contracts.ts";
import {
  ClosedBetaAiPolicy,
  SupabaseAccessDataSource,
} from "../_shared/access-policy.ts";
import {
  type AuthenticatedUser,
  authenticateSupabaseRequest,
  AuthError,
} from "../_shared/auth.ts";

const FEATURES: readonly AiFeature[] = [
  "SYLLABUS_GENERATION",
  "PLAN_GENERATION",
  "CONTENT_GENERATION",
  "SIMULATION_GENERATION",
];

export interface AiAccessHandlerDependencies {
  authenticate: (request: Request) => Promise<AuthenticatedUser>;
  policy?: Pick<ClosedBetaAiPolicy, "getAccess">;
  policyForUser?: (
    user: AuthenticatedUser,
  ) => Pick<ClosedBetaAiPolicy, "getAccess">;
}

function jsonResponse(
  body: unknown,
  status = 200,
  headers: Record<string, string> = {},
): Response {
  return new Response(JSON.stringify(body), {
    status,
    headers: {
      "content-type": "application/json; charset=utf-8",
      "cache-control": "no-store",
      ...headers,
    },
  });
}

function safeError(code: string, status: number): Response {
  return jsonResponse({
    error: {
      code,
      message: code === "AUTH_REQUIRED"
        ? "Authentication required"
        : "AI access is temporarily unavailable",
    },
  }, status);
}

function featureFromRequest(request: Request): AiFeature | null {
  const requested = new URL(request.url).searchParams.get("feature") ??
    "SYLLABUS_GENERATION";
  return FEATURES.includes(requested as AiFeature)
    ? requested as AiFeature
    : null;
}

export function createAiAccessHandler(
  dependencies: AiAccessHandlerDependencies,
): (request: Request) => Promise<Response> {
  return async (request: Request): Promise<Response> => {
    if (request.method !== "GET") {
      return jsonResponse(
        {
          error: {
            code: "METHOD_NOT_ALLOWED",
            message: "Only GET is supported",
          },
        },
        405,
        { allow: "GET" },
      );
    }

    const feature = featureFromRequest(request);
    if (!feature) return safeError("INVALID_FEATURE", 400);

    let user: AuthenticatedUser;
    try {
      user = await dependencies.authenticate(request);
    } catch (error) {
      if (error instanceof AuthError) {
        return safeError(error.code, error.status);
      }
      return safeError("AUTH_UNAVAILABLE", 503);
    }

    try {
      const policy = dependencies.policyForUser?.(user) ?? dependencies.policy;
      if (!policy) throw new Error("AI_ACCESS_POLICY_UNAVAILABLE");
      const access: AiAccess = await policy.getAccess(user.userId, feature);
      return jsonResponse(access);
    } catch {
      return safeError("AI_ACCESS_UNAVAILABLE", 503);
    }
  };
}

async function handleAiAccess(request: Request): Promise<Response> {
  return createAiAccessHandler({
    authenticate: authenticateSupabaseRequest,
    policyForUser: (user) => {
      const supabaseUrl = Deno.env.get("SUPABASE_URL")?.trim();
      const publishableKey = (
        Deno.env.get("SUPABASE_ANON_KEY") ??
          Deno.env.get("SUPABASE_PUBLISHABLE_KEY")
      )?.trim();
      if (!supabaseUrl || !publishableKey || !user.accessToken) {
        throw new Error("AI_ACCESS_CONTEXT_UNAVAILABLE");
      }
      return new ClosedBetaAiPolicy(
        new SupabaseAccessDataSource({
          supabaseUrl,
          authenticatedUserId: user.userId,
          accessToken: user.accessToken,
          publishableKey,
        }),
      );
    },
  })(request);
}

if (import.meta.main) {
  Deno.serve(handleAiAccess);
}
