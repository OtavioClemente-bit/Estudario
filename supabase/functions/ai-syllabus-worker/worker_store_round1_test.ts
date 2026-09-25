import { assert, assertRejects, assertStringIncludes } from "https://deno.land/std@0.224.0/assert/mod.ts";
import { authorizeWorkerRequest, LeaseLostError, SupabaseSyllabusWorkerStore, type Lease, workerBackendHeaders } from "./index.ts";

const lease: Lease = { owner: "worker-a", token: "token-a", generation: 7 };

Deno.test("persists cleanup before deletion and retries it after a worker restart", async () => {
  const calls: string[] = [];
  let retrySweep = false;
  const fetcher: typeof fetch = async (input, init) => {
    const url = String(input);
    calls.push(`${init?.method ?? "GET"} ${url}`);
    if (url.includes("/storage/v1/object/")) {
      return retrySweep ? new Response(null, { status: 404 }) : new Response("storage unavailable", { status: 503 });
    }
    if (url.includes("prepare_ai_job_source_cleanup")) {
      return Response.json([{ job_id: "job-cleanup", source_object_path: "user/source.pdf", status: "PENDING", lease_generation: 7 }]);
    }
    if (url.includes("fail_ai_job_source_cleanup")) return Response.json([{ status: "PENDING" }]);
    if (url.includes("claim_ai_job_source_cleanup")) {
      return Response.json([{ job_id: "job-cleanup", source_object_path: "user/source.pdf", lease_generation: 8 }]);
    }
    if (url.includes("complete_ai_job_source_cleanup")) return Response.json([{ status: "DELETED" }]);
    throw new Error(`unexpected request ${url}`);
  };
  const environment = { supabaseUrl: "https://supabase.test", serviceRoleKey: "service-test", fetcher };
  const storage = {} as never;
  const firstWorker = new SupabaseSyllabusWorkerStore(environment, storage, "worker-a");
  await assertRejects(() => firstWorker.cleanupSource("job-cleanup", lease), Error, "AI_SOURCE_CLEANUP_FAILED");
  assert(calls.some((call) => call.includes("prepare_ai_job_source_cleanup")));
  assert(calls.some((call) => call.includes("fail_ai_job_source_cleanup")));

  retrySweep = true;
  const restartedWorker = new SupabaseSyllabusWorkerStore(environment, storage, "worker-b");
  await restartedWorker.cleanupPendingSources();
  assert(calls.some((call) => call.includes("claim_ai_job_source_cleanup")));
  assert(calls.some((call) => call.includes("complete_ai_job_source_cleanup")));
  assertStringIncludes(calls.find((call) => call.includes("/storage/v1/object/")) ?? "", "user/source.pdf");
});

Deno.test("maps a PostgREST lease error to a non-mutating lease loss", async () => {
  const fetcher: typeof fetch = async () => Response.json({ code: "P0001", message: "AI_JOB_LEASE_LOST" }, { status: 400 });
  const store = new SupabaseSyllabusWorkerStore({ supabaseUrl: "https://supabase.test", serviceRoleKey: "service-test", fetcher }, {} as never, "worker-a");
  const error = await assertRejects(() => store.assertLease("job-cleanup", lease));
  assert(error instanceof LeaseLostError);
});

Deno.test("uses a legacy JWT for worker RPCs and never sends a secret key as Bearer", () => {
  const jwt = workerBackendHeaders({ serviceRoleKey: "sb_secret_backend-key", serviceRoleJwt: "legacy-service-role-jwt" }) as Record<string, string | undefined>;
  assert(jwt.apikey === "legacy-service-role-jwt");
  assert(jwt.authorization === "Bearer legacy-service-role-jwt");

  const secret = workerBackendHeaders({ serviceRoleKey: "sb_secret_backend-key" }) as Record<string, string | undefined>;
  assert(secret.apikey === "sb_secret_backend-key");
  assert(secret.authorization === undefined);
});

Deno.test("worker endpoint accepts only the exact backend token", () => {
  const request = (authorization?: string) => new Request("https://example.test/functions/v1/ai-syllabus-worker", {
    method: "POST", headers: authorization ? { authorization } : undefined,
  });
  assert(authorizeWorkerRequest(request("Bearer worker-token"), "worker-token"));
  assert(!authorizeWorkerRequest(request("Bearer wrong-token"), "worker-token"));
  assert(!authorizeWorkerRequest(request(), "worker-token"));
  assert(!authorizeWorkerRequest(request("Bearer worker-token"), undefined));
});

Deno.test("treats a nullable composite cleanup claim returned as [null] as empty", async () => {
  const fetcher: typeof fetch = async (input) => {
    if (String(input).includes("claim_ai_job_source_cleanup")) return Response.json([null]);
    throw new Error(`unexpected request ${String(input)}`);
  };
  const store = new SupabaseSyllabusWorkerStore({ supabaseUrl: "https://supabase.test", serviceRoleKey: "service-test", fetcher }, {} as never, "worker-a");
  await store.cleanupPendingSources();
});

Deno.test("treats a nullable composite worker claim returned as [null] as no job", async () => {
  const fetcher: typeof fetch = async (input) => {
    if (String(input).includes("claim_ai_syllabus_worker_job")) return Response.json([null]);
    throw new Error(`unexpected request ${String(input)}`);
  };
  const store = new SupabaseSyllabusWorkerStore({ supabaseUrl: "https://supabase.test", serviceRoleKey: "service-test", fetcher }, {} as never, "worker-a");
  const claimed = await store.claimNext(new Date("2026-09-25T00:00:00Z"), 300, 900);
  assert(claimed === null);
});

Deno.test("treats the cloud all-null composite cleanup claim as empty", async () => {
  const fetcher: typeof fetch = async (input) => {
    if (String(input).includes("claim_ai_job_source_cleanup")) return Response.json([{
      job_id: null,
      status: null,
      created_at: null,
      last_error: null,
      updated_at: null,
      lease_owner: null,
      lease_token: null,
      attempt_count: null,
      lease_expires_at: null,
      lease_generation: null,
      source_object_path: null,
    }]);
    throw new Error(`unexpected request ${String(input)}`);
  };
  const store = new SupabaseSyllabusWorkerStore({ supabaseUrl: "https://supabase.test", serviceRoleKey: "service-test", fetcher }, {} as never, "worker-a");
  await store.cleanupPendingSources();
});

Deno.test("does not treat a partially populated composite claim as empty", async () => {
  const fetcher: typeof fetch = async (input) => {
    if (String(input).includes("claim_ai_syllabus_worker_job")) return Response.json([{ job_id: "job-1", status: null }]);
    throw new Error(`unexpected request ${String(input)}`);
  };
  const store = new SupabaseSyllabusWorkerStore({ supabaseUrl: "https://supabase.test", serviceRoleKey: "service-test", fetcher }, {} as never, "worker-a");
  await assertRejects(() => store.claimNext(new Date("2026-09-25T00:00:00Z"), 300, 900), Error, "AI_WORKER_DATA_UNAVAILABLE");
});
