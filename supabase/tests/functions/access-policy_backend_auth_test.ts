import { strict as assert } from "node:assert";
import {
  AccessDataError,
  resolveAccessServiceRoleKey,
  SupabaseAccessDataSource,
} from "../../functions/_shared/access-policy.ts";

const USER_ID = "5e5ac836-8f7b-4de9-8e89-afc6a7983901";
const BASE_URL = "https://project.supabase.test";
const MODERN_KEY = "sb_secret_test_only_not_a_real_secret";
const LEGACY_JWT =
  "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoic2VydmljZV9yb2xlIn0.signature";

function responseFor(url: URL): Response {
  if (url.pathname.endsWith("/profiles")) {
    return Response.json([{ user_id: USER_ID, beta_access: true }]);
  }
  if (url.pathname.endsWith("/ai_feature_flags")) {
    return Response.json([{ flag_key: "AI_BETA_ENABLED", enabled: true }]);
  }
  if (url.pathname.endsWith("/ai_quota_usage")) {
    return Response.json([{
      user_id: USER_ID,
      feature: "SYLLABUS_GENERATION",
      successful_count: 0,
      reserved_count: 0,
      period_start: "1970-01-01",
    }]);
  }
  return Response.json({ code: "unexpected_path" }, { status: 404 });
}

async function readAllAccessTables(serviceRoleKey: string) {
  const requests: Request[] = [];
  const source = new SupabaseAccessDataSource({
    supabaseUrl: BASE_URL,
    serviceRoleKey,
    fetcher: async (input, init) => {
      const request = new Request(input, init);
      requests.push(request);
      return responseFor(new URL(request.url));
    },
  });

  assert.deepEqual(await source.findProfile(USER_ID), {
    userId: USER_ID,
    betaAccess: true,
  });
  assert.deepEqual(await source.listFeatureFlags(["AI_BETA_ENABLED"]), [
    { flagKey: "AI_BETA_ENABLED", enabled: true },
  ]);
  assert.deepEqual(
    await source.findQuotaUsage(USER_ID, "SYLLABUS_GENERATION", "1970-01-01"),
    {
      userId: USER_ID,
      feature: "SYLLABUS_GENERATION",
      successfulCount: 0,
      reservedCount: 0,
      periodStart: "1970-01-01",
    },
  );
  return requests;
}

Deno.test("opaque secret key authenticates each access-table read only through apikey", async () => {
  const requests = await readAllAccessTables(MODERN_KEY);

  assert.deepEqual(
    requests.map((request) => new URL(request.url).pathname.split("/").at(-1)),
    [
      "profiles",
      "ai_feature_flags",
      "ai_quota_usage",
    ],
  );
  for (const request of requests) {
    assert.equal(request.headers.get("apikey"), MODERN_KEY);
    assert.equal(request.headers.has("authorization"), false);
  }
});

Deno.test("legacy service-role JWT remains in both apikey and Bearer authorization", async () => {
  const requests = await readAllAccessTables(LEGACY_JWT);

  assert.equal(requests.length, 3);
  for (const request of requests) {
    assert.equal(request.headers.get("apikey"), LEGACY_JWT);
    assert.equal(request.headers.get("authorization"), `Bearer ${LEGACY_JWT}`);
  }
});

Deno.test("runtime key resolution prefers modern default key and falls back to legacy", () => {
  assert.equal(
    resolveAccessServiceRoleKey(
      JSON.stringify({ default: MODERN_KEY }),
      LEGACY_JWT,
    ),
    MODERN_KEY,
  );
  assert.equal(resolveAccessServiceRoleKey("not-json", LEGACY_JWT), LEGACY_JWT);
  assert.equal(
    resolveAccessServiceRoleKey(
      JSON.stringify({ other: MODERN_KEY }),
      LEGACY_JWT,
    ),
    LEGACY_JWT,
  );
  assert.equal(resolveAccessServiceRoleKey(null, "  "), null);
});

Deno.test("HTTP errors become sanitized AccessDataError without exposing either credential", async () => {
  const source = new SupabaseAccessDataSource({
    supabaseUrl: BASE_URL,
    serviceRoleKey: MODERN_KEY,
    fetcher: async () =>
      new Response(JSON.stringify({ message: MODERN_KEY }), { status: 401 }),
  });

  await assert.rejects(
    () => source.findQuotaUsage(USER_ID, "SYLLABUS_GENERATION", "1970-01-01"),
    (error: unknown) => {
      assert.ok(error instanceof AccessDataError);
      assert.equal(error.message, "AI_ACCESS_DATA_UNAVAILABLE");
      assert.equal(error.message.includes(MODERN_KEY), false);
      assert.equal(error.message.includes(LEGACY_JWT), false);
      return true;
    },
  );
});
