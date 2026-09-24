import { assert, assertEquals, assertRejects } from "https://deno.land/std@0.224.0/assert/mod.ts";
import { SupabasePrivateSyllabusStore } from "./index.ts";
import type { PrivateSyllabus } from "../_shared/contracts.ts";

const REMOTE_ID = "11111111-1111-4111-8111-111111111111";
const SUBJECT_ID = "22222222-2222-4222-8222-222222222222";
const TOPIC_ID = "33333333-3333-4333-8333-333333333333";

function syllabus(): PrivateSyllabus {
  return {
    remoteSyllabusId: REMOTE_ID,
    title: "Edital atômico",
    position: 0,
    visibility: "PRIVATE",
    source: "IMPORTED",
    sourceJobId: null,
    sourceHash: null,
    schemaVersion: 1,
    status: "ACTIVE",
    metadata: { packageId: "pkg-atomic", stableIdentity: "competition-official" },
    subjects: [{
      remoteSubjectId: SUBJECT_ID,
      externalId: "subject-official",
      name: "Direito",
      position: 0,
      suggestedPriority: "HIGH",
      packageVersion: "estudo-v2",
      schemaVersion: 1,
      metadata: { marker: "subject" },
      topics: [{
        remoteTopicId: TOPIC_ID,
        externalId: "topic-official",
        parentRemoteTopicId: null,
        name: "Constitucional",
        position: 0,
        packageVersion: "estudo-v2",
        schemaVersion: 1,
        metadata: { marker: "topic" },
        children: [],
      }],
    }],
  };
}

class TransactionalRpcFake {
  readonly calls: string[] = [];
  readonly roots = new Map<string, PrivateSyllabus>();
  private readonly ledger = new Map<string, { hash: string; remoteSyllabusId: string; response: Record<string, unknown> }>();
  private readonly active = new Map<string, { hash: string; remoteSyllabusId: string; done: Promise<Record<string, unknown>> }>();

  async fetch(input: RequestInfo | URL, init?: RequestInit): Promise<Response> {
    const url = String(input);
    this.calls.push(url);
    const isDelete = url.includes("/rest/v1/rpc/delete_private_syllabus_atomic");
    assert(isDelete || url.includes("/rest/v1/rpc/upsert_private_syllabus_atomic"), "mutation must use an atomic RPC");
    const body = JSON.parse(String(init?.body)) as {
      p_owner_user_id: string;
      p_mutation_id: string;
      p_payload_hash: string;
      p_remote_syllabus_id?: string;
      p_syllabus?: PrivateSyllabus;
    };
    const remoteSyllabusId = body.p_remote_syllabus_id ?? body.p_syllabus?.remoteSyllabusId;
    assert(remoteSyllabusId, "mutation must include a remote syllabus identity");
    const key = `${body.p_owner_user_id}:${body.p_mutation_id}`;
    const previous = this.ledger.get(key);
    if (previous) {
      if (previous.hash !== body.p_payload_hash || previous.remoteSyllabusId !== remoteSyllabusId) return new Response(JSON.stringify({ message: "IDEMPOTENCY_KEY_CONFLICT" }), { status: 409 });
      return Response.json(previous.response);
    }
    const running = this.active.get(key);
    if (running) {
      if (running.hash !== body.p_payload_hash || running.remoteSyllabusId !== remoteSyllabusId) return new Response(JSON.stringify({ message: "IDEMPOTENCY_KEY_CONFLICT" }), { status: 409 });
      return Response.json(await running.done);
    }
    let resolve!: (value: Record<string, unknown>) => void;
    const done = new Promise<Record<string, unknown>>((res) => { resolve = res; });
    this.active.set(key, { hash: body.p_payload_hash, remoteSyllabusId, done });
    await Promise.resolve();
    const response = {
      remoteSyllabusId,
      jobId: null,
      payloadHash: body.p_payload_hash,
      state: "SYNCED",
      attemptCount: 1,
      nextAttemptAt: null,
      safeError: null,
      createdAt: "2026-09-24T00:00:00Z",
      updatedAt: "2026-09-24T00:00:00Z",
      attemptToken: null,
    };
    if (isDelete) this.roots.delete(remoteSyllabusId);
    else {
      assert(body.p_syllabus, "upsert must include a syllabus tree");
      this.roots.set(remoteSyllabusId, structuredClone(body.p_syllabus));
    }
    this.ledger.set(key, { hash: body.p_payload_hash, remoteSyllabusId, response });
    this.active.delete(key);
    resolve(response);
    return Response.json(response);
  }
}

Deno.test("upsert uses one transactional RPC and serializes same-mutation replay", async () => {
  const fake = new TransactionalRpcFake();
  const store = new SupabasePrivateSyllabusStore({
    supabaseUrl: "https://example.invalid",
    serviceRoleKey: "test-only",
    fetcher: fake.fetch.bind(fake),
  });
  const value = syllabus();
  const first = store.upsert({ ownerId: "owner-a", mutationId: "mutation-1", payloadHash: "a".repeat(64), syllabus: value });
  const second = store.upsert({ ownerId: "owner-a", mutationId: "mutation-1", payloadHash: "a".repeat(64), syllabus: value });
  const acknowledgements = await Promise.all([first, second]);

  assertEquals(acknowledgements[0].remoteSyllabusId, REMOTE_ID);
  assertEquals(acknowledgements[1], acknowledgements[0]);
  assertEquals(fake.calls.length, 2);
  assertEquals(fake.roots.get(REMOTE_ID), value);

  await assertRejects(
    () => store.upsert({ ownerId: "owner-a", mutationId: "mutation-1", payloadHash: "b".repeat(64), syllabus: value }),
    Error,
    "IDEMPOTENCY_KEY_CONFLICT",
  );

  const concurrentConflict = await Promise.allSettled([
    store.upsert({ ownerId: "owner-a", mutationId: "mutation-2", payloadHash: "c".repeat(64), syllabus: value }),
    store.upsert({ ownerId: "owner-a", mutationId: "mutation-2", payloadHash: "d".repeat(64), syllabus: value }),
  ]);
  assertEquals(concurrentConflict.filter((item) => item.status === "fulfilled").length, 1);
  assertEquals(concurrentConflict.filter((item) => item.status === "rejected").length, 1);
});

Deno.test("delete uses one transactional RPC and replays concurrent duplicate mutations", async () => {
  const fake = new TransactionalRpcFake();
  const store = new SupabasePrivateSyllabusStore({
    supabaseUrl: "https://example.invalid",
    serviceRoleKey: "test-only",
    fetcher: fake.fetch.bind(fake),
  });
  const value = syllabus();
  await store.upsert({ ownerId: "owner-a", mutationId: "mutation-seed", payloadHash: "a".repeat(64), syllabus: value });

  const acknowledgements = await Promise.all([
    store.delete({ ownerId: "owner-a", remoteSyllabusId: REMOTE_ID, mutationId: "delete-1", payloadHash: "b".repeat(64) }),
    store.delete({ ownerId: "owner-a", remoteSyllabusId: REMOTE_ID, mutationId: "delete-1", payloadHash: "b".repeat(64) }),
  ]);
  assertEquals(acknowledgements[0], acknowledgements[1]);
  assertEquals(fake.roots.has(REMOTE_ID), false);

  await assertRejects(
    () => store.delete({ ownerId: "owner-a", remoteSyllabusId: REMOTE_ID, mutationId: "delete-1", payloadHash: "c".repeat(64) }),
    Error,
    "IDEMPOTENCY_KEY_CONFLICT",
  );
});
