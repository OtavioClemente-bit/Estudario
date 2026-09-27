import { strict as assert } from "node:assert";
import {
  AccessDataError,
  SupabaseAccessDataSource,
} from "../../functions/_shared/access-policy.ts";

const USER_ID = "5e5ac836-8f7b-4de9-8e89-afc6a7983901";
const OTHER_USER_ID = "00000000-0000-0000-0000-0000000000b1";
const BASE_URL = "https://project.supabase.test";
const PUBLISHABLE_KEY = "sb_publishable_test_client_key";
const USER_JWT = "user-scoped-test-jwt";
const SECRET_KEY_SENTINEL = "sb_secret_never_used_by_ai_access";

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

async function readAllAccessTables() {
  const requests: Request[] = [];
  const source = new SupabaseAccessDataSource({
    supabaseUrl: BASE_URL,
    authenticatedUserId: USER_ID,
    accessToken: USER_JWT,
    publishableKey: PUBLISHABLE_KEY,
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

Deno.test("all access-table reads use the authenticated JWT and client apikey", async () => {
  const requests = await readAllAccessTables();

  assert.deepEqual(
    requests.map((request) => new URL(request.url).pathname.split("/").at(-1)),
    [
      "profiles",
      "ai_feature_flags",
      "ai_quota_usage",
    ],
  );
  for (const request of requests) {
    assert.equal(request.headers.get("apikey"), PUBLISHABLE_KEY);
    assert.equal(request.headers.get("authorization"), `Bearer ${USER_JWT}`);
    assert.notEqual(request.headers.get("apikey"), SECRET_KEY_SENTINEL);
    assert.notEqual(
      request.headers.get("authorization"),
      `Bearer ${SECRET_KEY_SENTINEL}`,
    );
  }
});

Deno.test("user-scoped data source refuses profile or quota queries for another user", async () => {
  const requests: Request[] = [];
  const source = new SupabaseAccessDataSource({
    supabaseUrl: BASE_URL,
    authenticatedUserId: USER_ID,
    accessToken: USER_JWT,
    publishableKey: PUBLISHABLE_KEY,
    fetcher: async (input, init) => {
      requests.push(new Request(input, init));
      return responseFor(new URL(String(input)));
    },
  });

  await assert.rejects(
    () => source.findProfile(OTHER_USER_ID),
    AccessDataError,
  );
  await assert.rejects(
    () =>
      source.findQuotaUsage(OTHER_USER_ID, "SYLLABUS_GENERATION", "1970-01-01"),
    AccessDataError,
  );
  assert.equal(requests.length, 0);
});

Deno.test("HTTP errors become sanitized AccessDataError without exposing credentials", async () => {
  const source = new SupabaseAccessDataSource({
    supabaseUrl: BASE_URL,
    authenticatedUserId: USER_ID,
    accessToken: USER_JWT,
    publishableKey: PUBLISHABLE_KEY,
    fetcher: async () =>
      new Response(
        JSON.stringify({ message: `${SECRET_KEY_SENTINEL} ${USER_JWT}` }),
        { status: 401 },
      ),
  });

  await assert.rejects(
    () => source.findQuotaUsage(USER_ID, "SYLLABUS_GENERATION", "1970-01-01"),
    (error: unknown) => {
      assert.ok(error instanceof AccessDataError);
      assert.equal(error.message, "AI_ACCESS_DATA_UNAVAILABLE");
      assert.equal(error.message.includes(SECRET_KEY_SENTINEL), false);
      assert.equal(error.message.includes(USER_JWT), false);
      assert.equal(error.message.includes(PUBLISHABLE_KEY), false);
      return true;
    },
  );
});
