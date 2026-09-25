import { strict as assert } from "node:assert";
import {
  ClosedBetaAiPolicy,
  type AccessDataSource,
  type FeatureFlagRecord,
  type ProfileRecord,
  type QuotaUsageRecord,
} from "../../functions/_shared/access-policy.ts";
import {
  authenticateSupabaseRequest,
  AuthError,
  type AuthEnvironment,
} from "../../functions/_shared/auth.ts";
import { createAiAccessHandler } from "../../functions/ai-access/index.ts";

const USER_A = "00000000-0000-0000-0000-0000000000a1";
const USER_B = "00000000-0000-0000-0000-0000000000b1";
const FEATURE = "SYLLABUS_GENERATION" as const;

class FakeAccessDataSource implements AccessDataSource {
  constructor(
    private readonly profiles: ProfileRecord[],
    private readonly flags: FeatureFlagRecord[],
    private readonly quotas: QuotaUsageRecord[],
  ) {}

  async findProfile(userId: string): Promise<ProfileRecord | null> {
    return this.profiles.find((profile) => profile.userId === userId) ?? null;
  }

  async listFeatureFlags(keys: readonly string[]): Promise<FeatureFlagRecord[]> {
    return this.flags.filter((flag) => keys.includes(flag.flagKey));
  }

  async findQuotaUsage(userId: string, feature: string, periodStart: string): Promise<QuotaUsageRecord | null> {
    return this.quotas.find((quota) =>
      quota.userId === userId && quota.feature === feature && quota.periodStart === periodStart
    ) ?? null;
  }
}

function flags(overrides: Partial<Record<string, boolean>> = {}): FeatureFlagRecord[] {
  return [
    { flagKey: "AI_BETA_ENABLED", enabled: overrides.AI_BETA_ENABLED ?? true },
    { flagKey: "SYLLABUS_AI_ENABLED", enabled: overrides.SYLLABUS_AI_ENABLED ?? true },
    { flagKey: "PLAN_AI_ENABLED", enabled: overrides.PLAN_AI_ENABLED ?? true },
    { flagKey: "CONTENT_AI_ENABLED", enabled: overrides.CONTENT_AI_ENABLED ?? true },
  ];
}

function policyFor(
  data: Partial<{
    profiles: ProfileRecord[];
    flags: FeatureFlagRecord[];
    quotas: QuotaUsageRecord[];
  }> = {},
  now = new Date("2026-09-23T12:00:00Z"),
): ClosedBetaAiPolicy {
  return new ClosedBetaAiPolicy(
    new FakeAccessDataSource(
      data.profiles ?? [{ userId: USER_A, betaAccess: true }],
      data.flags ?? flags(),
      data.quotas ?? [],
    ),
    { now: () => now },
  );
}

function quota(
  userId: string,
  feature: string,
  successfulCount: number,
  reservedCount: number,
  periodStart = "1970-01-01",
): QuotaUsageRecord {
  return { userId, feature, successfulCount, reservedCount, periodStart };
}

Deno.test("allows a beta-enabled account with available syllabus quota", async () => {
  const policy = policyFor();
  const access = await policy.getAccess(USER_A, FEATURE);

  assert.equal(access.authenticated, true);
  assert.equal(access.betaAccess, true);
  assert.equal(access.featureEnabled, true);
  assert.equal(access.canUse, true);
  assert.equal(await policy.canUse(USER_A, FEATURE), true);
  assert.equal(access.reasonCode, null);
  assert.deepEqual(access.quota, {
    feature: FEATURE,
    limit: 1,
    successfulCount: 0,
    reservedCount: 0,
    used: 0,
    remaining: 1,
    periodStart: "1970-01-01",
    resetAt: null,
  });
});

Deno.test("denies a beta-disabled account even when its feature and quota are available", async () => {
  const access = await policyFor({
    profiles: [{ userId: USER_A, betaAccess: false }],
  }).getAccess(USER_A, FEATURE);

  assert.equal(access.betaAccess, false);
  assert.equal(access.canUse, false);
  assert.equal(access.reasonCode, "BETA_ACCESS_REQUIRED");
});

Deno.test("denies a feature whose server-side flag is disabled", async () => {
  const access = await policyFor({
    flags: flags({ SYLLABUS_AI_ENABLED: false }),
  }).getAccess(USER_A, FEATURE);
  const betaDisabled = await policyFor({
    flags: flags({ AI_BETA_ENABLED: false }),
  }).getAccess(USER_A, FEATURE);

  assert.equal(access.featureEnabled, false);
  assert.equal(access.canUse, false);
  assert.equal(access.reasonCode, "FEATURE_DISABLED");
  assert.equal(betaDisabled.featureEnabled, false);
  assert.equal(betaDisabled.canUse, false);
  assert.equal(betaDisabled.reasonCode, "BETA_DISABLED");
});

Deno.test("reports exhausted and reserved quota without trusting client-provided counts", async () => {
  const exhausted = await policyFor({
    quotas: [quota(USER_A, FEATURE, 1, 0)],
  }).getAccess(USER_A, FEATURE);
  const reserved = await policyFor({
    quotas: [quota(USER_A, FEATURE, 0, 1)],
  }).getAccess(USER_A, FEATURE);

  assert.equal(exhausted.canUse, false);
  assert.equal(exhausted.reasonCode, "QUOTA_EXHAUSTED");
  assert.equal(exhausted.quota?.used, 1);
  assert.equal(exhausted.quota?.remaining, 0);
  assert.equal(exhausted.quota?.resetAt, null);
  assert.equal(reserved.canUse, false);
  assert.equal(reserved.reasonCode, "QUOTA_RESERVED");
  assert.equal(reserved.quota?.reservedCount, 1);
});

Deno.test("isolates account A quota from account B quota", async () => {
  const policy = policyFor({
    profiles: [
      { userId: USER_A, betaAccess: true },
      { userId: USER_B, betaAccess: true },
    ],
    quotas: [quota(USER_A, FEATURE, 0, 0), quota(USER_B, FEATURE, 1, 0)],
  });

  const accountA = await policy.getAccess(USER_A, FEATURE);
  const accountB = await policy.getAccess(USER_B, FEATURE);

  assert.equal(accountA.canUse, true);
  assert.equal(accountB.canUse, false);
  assert.equal(accountB.reasonCode, "QUOTA_EXHAUSTED");
});

Deno.test("uses America/Sao_Paulo when selecting a daily quota period", async () => {
  const contentFeature = "CONTENT_GENERATION" as const;
  const access = await policyFor({
    quotas: [quota(USER_A, contentFeature, 0, 0, "2026-09-22")],
  }, new Date("2026-09-23T02:30:00Z")).getAccess(USER_A, contentFeature);

  assert.equal(access.canUse, true);
  assert.equal(access.quota?.periodStart, "2026-09-22");
  assert.equal(access.quota?.resetAt, "2026-09-23T03:00:00.000Z");
});

Deno.test("plan has no reset and exhausted content has the next Sao Paulo midnight", async () => {
  const plan = await policyFor().getAccess(USER_A, "PLAN_GENERATION");
  const content = await policyFor({ quotas: [quota(USER_A, "CONTENT_GENERATION", 1, 0, "2026-09-23")] }).getAccess(USER_A, "CONTENT_GENERATION");
  assert.equal(plan.quota?.resetAt, null);
  assert.equal(content.quota?.remaining, 0);
  assert.equal(content.quota?.resetAt, "2026-09-24T03:00:00.000Z");
});

Deno.test("GET access is read-only, ignores Android account/quota claims, and ignores Drive authorization", async () => {
  const handler = createAiAccessHandler({
    authenticate: async () => ({ userId: USER_A }),
    policy: policyFor({
      profiles: [
        { userId: USER_A, betaAccess: true },
        { userId: USER_B, betaAccess: true },
      ],
      quotas: [quota(USER_A, FEATURE, 1, 0), quota(USER_B, FEATURE, 0, 0)],
    }),
  });
  const response = await handler(new Request(
    "https://example.test/functions/v1/ai-access?feature=SYLLABUS_GENERATION&accountId=" + USER_B + "&remaining=999",
    {
      headers: {
        Authorization: "Bearer supabase-jwt",
        "X-Goog-Drive-Token": "drive-token-that-must-not-authorize-ai",
      },
    },
  ));
  const body = await response.json();

  assert.equal(response.status, 200);
  assert.equal(body.authenticated, true);
  assert.equal(body.canUse, false);
  assert.equal(body.reasonCode, "QUOTA_EXHAUSTED");
  assert.equal(body.quota.used, 1);
  assert.equal(body.quota.remaining, 0);
  assert.equal(body.quota.resetAt, null);

  const postResponse = await handler(new Request("https://example.test/functions/v1/ai-access", { method: "POST" }));
  assert.equal(postResponse.status, 405);
  assert.equal(postResponse.headers.get("allow"), "GET");
});

Deno.test("authenticates the Supabase bearer token server-side and returns safe auth errors", async () => {
  const requests: Request[] = [];
  const environment: AuthEnvironment = {
    supabaseUrl: "https://project.supabase.co",
    publishableKey: "publishable-key-from-server-environment",
    fetcher: async (input, init) => {
      requests.push(new Request(input, init));
      return new Response(JSON.stringify({ id: USER_A }), { status: 200 });
    },
  };
  const user = await authenticateSupabaseRequest(
    new Request("https://example.test", { headers: { Authorization: "Bearer verified-jwt" } }),
    environment,
  );

  assert.equal(user.userId, USER_A);
  assert.equal(requests.length, 1);
  assert.equal(requests[0].headers.get("authorization"), "Bearer verified-jwt");
  assert.equal(requests[0].headers.get("apikey"), "publishable-key-from-server-environment");

  await assert.rejects(
    () => authenticateSupabaseRequest(new Request("https://example.test"), environment),
    (error: unknown) => error instanceof AuthError && error.code === "AUTH_REQUIRED" && error.status === 401,
  );
});

Deno.test("does not expose server or provider errors in the access response", async () => {
  const handler = createAiAccessHandler({
    authenticate: async () => ({ userId: USER_A }),
    policy: {
      getAccess: async () => {
        throw new Error("internal provider detail must stay private");
      },
    },
  });
  const response = await handler(new Request("https://example.test/functions/v1/ai-access"));
  const body = await response.text();

  assert.equal(response.status, 503);
  assert.equal(body.includes("internal provider detail"), false);
  assert.equal(body.includes("stack"), false);
  assert.equal(body.includes("AI_ACCESS_UNAVAILABLE"), true);
});
