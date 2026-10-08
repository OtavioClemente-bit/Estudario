import { withCors } from "../_shared/cors.ts";
import {
  ClosedBetaAiPolicy,
  SupabaseAccessDataSource,
} from "../_shared/access-policy.ts";
import {
  type AuthenticatedUser,
  authenticateSupabaseRequest,
  AuthError,
} from "../_shared/auth.ts";

/**
 * GET  /ai-plan             -> plano efetivo, catálogo dos planos e uso atual (somente leitura).
 * POST /ai-plan/ad-interest -> registra o interesse em anúncio recompensado. Não concede crédito:
 *                              no teste fechado nenhum anúncio é exibido.
 */
export interface AiPlanHandlerDependencies {
  authenticate: (request: Request) => Promise<AuthenticatedUser>;
  policyForUser: (user: AuthenticatedUser) => Pick<ClosedBetaAiPolicy, "getPlanSummary">;
  registerAdInterest: (user: AuthenticatedUser) => Promise<number>;
  /** Catálogo com preços (tabela plan_catalog), para a tela de planos. */
  pricing: (user: AuthenticatedUser) => Promise<PlanPricing[]>;
  /** As assinaturas estão abertas (conta de serviço do Play configurada). */
  playEnabled: () => boolean;
}

export interface PlanPricing {
  planTier: string;
  name: string;
  tagline: string;
  priceMonthCents: number;
  priceYearCents: number;
  playProductId: string | null;
  playBasePlanMonth: string | null;
  playBasePlanYear: string | null;
  featured: boolean;
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

function safeError(code: string, status: number): Response {
  return jsonResponse({
    error: {
      code,
      message: code === "AUTH_REQUIRED" ? "Authentication required" : "Plan data is temporarily unavailable",
    },
  }, status);
}

export function createAiPlanHandler(
  dependencies: AiPlanHandlerDependencies,
): (request: Request) => Promise<Response> {
  return async (request: Request): Promise<Response> => {
    const path = new URL(request.url).pathname.replace(/\/+$/, "");
    const adInterest = path.endsWith("/ad-interest");
    const expected = adInterest ? "POST" : "GET";
    if (request.method !== expected) {
      return jsonResponse(
        { error: { code: "METHOD_NOT_ALLOWED", message: `Only ${expected} is supported` } },
        405,
        { allow: expected },
      );
    }

    let user: AuthenticatedUser;
    try {
      user = await dependencies.authenticate(request);
    } catch (error) {
      if (error instanceof AuthError) return safeError(error.code, error.status);
      return safeError("AUTH_UNAVAILABLE", 503);
    }

    try {
      if (adInterest) {
        const taps = await dependencies.registerAdInterest(user);
        return jsonResponse({ recorded: true, rewardGranted: false, tapsToday: taps });
      }
      const summary = await dependencies.policyForUser(user).getPlanSummary(user.userId);
      return jsonResponse({ ...summary, pricing: await dependencies.pricing(user), billing: { playEnabled: dependencies.playEnabled() } });
    } catch {
      return safeError("AI_PLAN_UNAVAILABLE", 503);
    }
  };
}

function environment(user: AuthenticatedUser): { supabaseUrl: string; publishableKey: string; accessToken: string } {
  const supabaseUrl = Deno.env.get("SUPABASE_URL")?.trim();
  const publishableKey = (Deno.env.get("SUPABASE_ANON_KEY") ?? Deno.env.get("SUPABASE_PUBLISHABLE_KEY"))?.trim();
  if (!supabaseUrl || !publishableKey || !user.accessToken) throw new Error("AI_PLAN_CONTEXT_UNAVAILABLE");
  return { supabaseUrl, publishableKey, accessToken: user.accessToken };
}

async function registerAdInterest(user: AuthenticatedUser): Promise<number> {
  const { supabaseUrl, publishableKey, accessToken } = environment(user);
  // Chamada com o JWT da própria pessoa: a função no banco usa auth.uid(), sem service_role.
  const response = await fetch(`${supabaseUrl.replace(/\/$/, "")}/rest/v1/rpc/register_ad_reward_interest`, {
    method: "POST",
    headers: {
      apikey: publishableKey,
      authorization: `Bearer ${accessToken}`,
      "content-type": "application/json",
      accept: "application/json",
    },
    body: "{}",
  });
  if (!response.ok) throw new Error("AD_INTEREST_UNAVAILABLE");
  const taps = await response.json();
  if (typeof taps !== "number") throw new Error("AD_INTEREST_UNAVAILABLE");
  return taps;
}

async function loadPricing(user: AuthenticatedUser): Promise<PlanPricing[]> {
  const { supabaseUrl, publishableKey, accessToken } = environment(user);
  const response = await fetch(`${supabaseUrl.replace(/\/$/, "")}/rest/v1/plan_catalog?select=*&order=sort_order.asc`, {
    headers: { apikey: publishableKey, authorization: `Bearer ${accessToken}`, accept: "application/json" },
  });
  if (!response.ok) return [];
  const rows = await response.json() as Record<string, unknown>[];
  return rows.map((row) => ({
    planTier: String(row.plan_tier ?? "FREE"),
    name: String(row.name ?? ""),
    tagline: String(row.tagline ?? ""),
    priceMonthCents: Number(row.price_month_cents ?? 0),
    priceYearCents: Number(row.price_year_cents ?? 0),
    playProductId: typeof row.play_product_id === "string" ? row.play_product_id : null,
    playBasePlanMonth: typeof row.play_base_plan_month === "string" ? row.play_base_plan_month : null,
    playBasePlanYear: typeof row.play_base_plan_year === "string" ? row.play_base_plan_year : null,
    featured: row.featured === true,
  }));
}

async function handleAiPlan(request: Request): Promise<Response> {
  return createAiPlanHandler({
    authenticate: authenticateSupabaseRequest,
    policyForUser: (user) => {
      const { supabaseUrl, publishableKey, accessToken } = environment(user);
      return new ClosedBetaAiPolicy(
        new SupabaseAccessDataSource({
          supabaseUrl,
          authenticatedUserId: user.userId,
          accessToken,
          publishableKey,
        }),
      );
    },
    registerAdInterest,
    pricing: loadPricing,
    playEnabled: () => !!Deno.env.get("PLAY_SERVICE_ACCOUNT_JSON")?.trim(),
  })(request);
}

if (import.meta.main) {
  Deno.serve(withCors(handleAiPlan));
}
