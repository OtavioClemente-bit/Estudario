import { assertEquals } from "https://deno.land/std@0.224.0/assert/mod.ts";
import { AuthError } from "../_shared/auth.ts";
import { createAiPlanHandler } from "./index.ts";

const user = { userId: "u", accessToken: "t" };

const extras = { pricing: () => Promise.resolve([]), playEnabled: () => true };

function handler(taps: number[] = [], tier: "FREE" | "ESSENCIAL" = "FREE", planToday?: number) {
  return createAiPlanHandler({
    authenticate: () => Promise.resolve(user),
    policyForUser: () => ({
      getPlanSummary: () =>
        Promise.resolve({ planTier: tier, planRenewsAt: null, betaAccess: true, plans: [], usage: [] }),
    }),
    registerAdInterest: () => {
      taps.push(1);
      return Promise.resolve(taps.length);
    },
    ...extras,
    ...(planToday === undefined ? {} : { planToday: () => Promise.resolve(planToday) }),
  });
}

Deno.test("GET returns plan summary", async () => {
  const response = await handler()(new Request("https://x/functions/v1/ai-plan"));
  assertEquals(response.status, 200);
  assertEquals((await response.json()).planTier, "FREE");
});

Deno.test("paid summary brings how many plan materials came free today", async () => {
  const body = await (await handler([], "ESSENCIAL", 3)(new Request("https://x/functions/v1/ai-plan"))).json();
  assertEquals(body.planToday, { used: 3, limit: 4 });
  const free = await (await handler([], "FREE", 3)(new Request("https://x/functions/v1/ai-plan"))).json();
  assertEquals(free.planToday, undefined);
});

Deno.test("ad interest never grants a reward", async () => {
  const taps: number[] = [];
  const response = await handler(taps)(new Request("https://x/functions/v1/ai-plan/ad-interest", { method: "POST" }));
  assertEquals(response.status, 200);
  const body = await response.json();
  assertEquals(body.rewardGranted, false);
  assertEquals(taps.length, 1);
});

Deno.test("wrong method is rejected", async () => {
  const response = await handler()(new Request("https://x/functions/v1/ai-plan/ad-interest"));
  assertEquals(response.status, 405);
});

Deno.test("unauthenticated request is rejected", async () => {
  const response = await createAiPlanHandler({
    authenticate: () => Promise.reject(new AuthError("AUTH_REQUIRED", 401)),
    policyForUser: () => {
      throw new Error("unreachable");
    },
    registerAdInterest: () => Promise.reject(new Error("unreachable")),
    ...extras,
  })(new Request("https://x/functions/v1/ai-plan"));
  assertEquals(response.status, 401);
});
