import { assertEquals } from "https://deno.land/std@0.224.0/assert/mod.ts";
import {
  type AccessDataSource,
  ClosedBetaAiPolicy,
  effectivePlanTier,
  billingCycle,
  freeCycleEnd,
  nextMonthlyReset,
  type PlanLimitRecord,
  type ProfileRecord,
  type QuotaUsageRecord,
  quotaPeriodStart,
} from "./access-policy.ts";

const LIMITS: PlanLimitRecord[] = [
  { planTier: "FREE", feature: "SYLLABUS_GENERATION", periodKind: "LIFETIME", quotaLimit: 1, maxPerRequest: null, maxPerTopicMonth: null },
  { planTier: "FREE", feature: "CONTENT_GENERATION", periodKind: "MONTHLY", quotaLimit: 10, maxPerRequest: null, maxPerTopicMonth: null },
  { planTier: "PRO", feature: "SYLLABUS_GENERATION", periodKind: "MONTHLY", quotaLimit: 8, maxPerRequest: null, maxPerTopicMonth: null },
  { planTier: "PRO", feature: "CONTENT_GENERATION", periodKind: "MONTHLY", quotaLimit: 200, maxPerRequest: null, maxPerTopicMonth: null },
];

function source(profile: ProfileRecord, usage: QuotaUsageRecord[] = []): AccessDataSource {
  return {
    findProfile: () => Promise.resolve(profile),
    listFeatureFlags: (keys) => Promise.resolve(keys.map((flagKey) => ({ flagKey, enabled: true }))),
    findQuotaUsage: (_userId, feature, periodStart) =>
      Promise.resolve(usage.find((row) => row.feature === feature && row.periodStart === periodStart) ?? null),
    listPlanLimits: (tier) => Promise.resolve(tier ? LIMITS.filter((row) => row.planTier === tier) : LIMITS),
  };
}

const NOW = new Date("2026-09-29T15:00:00Z");

Deno.test("expired paid plan falls back to free", () => {
  assertEquals(effectivePlanTier({ userId: "u", betaAccess: true, planTier: "PRO", planRenewsAt: "2026-09-01T00:00:00Z" }, NOW), "FREE");
  assertEquals(effectivePlanTier({ userId: "u", betaAccess: true, planTier: "PRO", planRenewsAt: null }, NOW), "PRO");
});

Deno.test("monthly periods use Sao Paulo calendar", () => {
  assertEquals(quotaPeriodStart("MONTHLY", NOW), "2026-09-01");
  assertEquals(quotaPeriodStart("LIFETIME", NOW), "1970-01-01");
  // TESTE: o saldo é do ciclo aberto no último reset do administrador.
  assertEquals(quotaPeriodStart("LIFETIME", NOW, "TESTE", "2026-10-08"), "2026-10-08");
  assertEquals(quotaPeriodStart("LIFETIME", NOW, "TESTE", null), "1970-01-02");
  assertEquals(quotaPeriodStart("MONTHLY", NOW, "TESTE", "2026-10-08"), "2026-09-01");
  assertEquals(nextMonthlyReset(NOW), "2026-10-01T03:00:00.000Z");
});

Deno.test("free syllabus is a single lifetime generation", async () => {
  const policy = new ClosedBetaAiPolicy(
    source({ userId: "u", betaAccess: true, planTier: "FREE" }, [
      { userId: "u", feature: "SYLLABUS_GENERATION", successfulCount: 1, reservedCount: 0, periodStart: "1970-01-01" },
    ]),
    { now: () => NOW },
  );
  const access = await policy.getAccess("u", "SYLLABUS_GENERATION");
  assertEquals(access.quota?.limit, 1);
  assertEquals(access.reasonCode, "QUOTA_EXHAUSTED");
  assertEquals(access.quota?.resetAt, null);
});

Deno.test("pro content quota is monthly and counts reservations", async () => {
  const policy = new ClosedBetaAiPolicy(
    source({ userId: "u", betaAccess: true, planTier: "PRO" }, [
      { userId: "u", feature: "CONTENT_GENERATION", successfulCount: 5, reservedCount: 1, periodStart: "2026-09-01" },
    ]),
    { now: () => NOW },
  );
  const access = await policy.getAccess("u", "CONTENT_GENERATION");
  assertEquals(access.quota?.limit, 200);
  assertEquals(access.quota?.remaining, 194);
  assertEquals(access.canUse, true);
});

Deno.test("feature without configured limit is closed", async () => {
  const policy = new ClosedBetaAiPolicy(source({ userId: "u", betaAccess: true, planTier: "FREE" }), { now: () => NOW });
  const access = await policy.getAccess("u", "PLAN_GENERATION");
  assertEquals(access.quota?.limit, 0);
  assertEquals(access.canUse, false);
});

Deno.test("plan summary lists catalog and usage", async () => {
  const policy = new ClosedBetaAiPolicy(source({ userId: "u", betaAccess: true, planTier: "FREE" }), { now: () => NOW });
  const summary = await policy.getPlanSummary("u");
  assertEquals(summary.planTier, "FREE");
  assertEquals(summary.plans.map((plan) => plan.planTier), ["FREE", "ESSENCIAL", "PRO"]);
  assertEquals(summary.usage.find((row) => row.feature === "CONTENT_GENERATION")?.remaining, 10);
});

Deno.test("cota de quem paga renova no dia do pagamento", () => {
  const now = new Date("2026-11-06T15:00:00Z");
  // pagou dia 28/10, próxima cobrança 28/11: ciclo 28/10 a 28/11
  assertEquals(billingCycle("2026-11-28T13:00:00Z", now).start, "2026-10-28");
  assertEquals(billingCycle("2026-11-28T13:00:00Z", now).next, "2026-11-28T13:00:00Z");
  // pagou dia 4: ciclo começou 04/11
  assertEquals(billingCycle("2026-12-04T13:00:00Z", now).start, "2026-11-04");
  // anual: ciclo mensal dentro do ano, renova todo dia 10
  const y = billingCycle("2027-03-10T13:00:00Z", now);
  assertEquals(y.start, "2026-10-10");
  assertEquals(y.next, "2026-11-10T03:00:00.000Z");
  // dia 31 em mês curto vira o último dia
  assertEquals(billingCycle("2026-12-31T13:00:00Z", new Date("2026-12-01T15:00:00Z")).start, "2026-11-30");
  assertEquals(quotaPeriodStart("MONTHLY", now, "PRO", null, "2026-11-28T13:00:00Z"), "2026-10-28");
  assertEquals(quotaPeriodStart("MONTHLY", now, "FREE", null, null), "2026-11-01");
});

Deno.test("Grátis renova um mês depois do primeiro uso", () => {
  const now = new Date("2026-11-06T15:00:00Z");
  assertEquals(quotaPeriodStart("MONTHLY", now, "FREE", null, null, "2026-10-15"), "2026-10-15");
  assertEquals(freeCycleEnd("2026-10-15"), "2026-11-15T03:00:00.000Z");
  assertEquals(freeCycleEnd("2027-01-31"), "2027-02-28T03:00:00.000Z");
});
