import { strict as assert } from "node:assert";
import {
  createUserSyllabiHandler,
  type UserSyllabiDependencies,
  type UserSyllabiStore,
} from "./index.ts";
import type {
  PrivateSyllabus,
  RemoteSyllabusSyncAcknowledgement,
} from "../_shared/contracts.ts";

const USER_A = "00000000-0000-0000-0000-0000000000a1";
const USER_B = "00000000-0000-0000-0000-0000000000b1";
const HASH_A = "a".repeat(64);

function syllabus(remoteSyllabusId = "remote-a"): PrivateSyllabus {
  return {
    remoteSyllabusId,
    title: "Edital preservado",
    position: 2,
    visibility: "PRIVATE",
    source: "AI_GENERATED",
    sourceJobId: null,
    sourceHash: HASH_A,
    schemaVersion: 1,
    status: "ACTIVE",
    metadata: { payloadHash: HASH_A, root: { source: "local" } },
    subjects: [{
      remoteSubjectId: "subject-a",
      externalId: "subject-external-a",
      name: "Constitucional",
      position: 1,
      suggestedPriority: "HIGH",
      packageVersion: "estudo-v2",
      schemaVersion: 1,
      metadata: { color: "blue" },
      topics: [{
        remoteTopicId: "topic-a",
        externalId: "topic-external-a",
        parentRemoteTopicId: null,
        name: "Direitos fundamentais",
        position: 0,
        packageVersion: "estudo-v2",
        schemaVersion: 1,
        metadata: { page: 42 },
        children: [{
          remoteTopicId: "topic-child-a",
          externalId: "topic-child-external-a",
          parentRemoteTopicId: "topic-a",
          name: "Remédios constitucionais",
          position: 3,
          packageVersion: "estudo-v2",
          schemaVersion: 1,
          metadata: { page: 43 },
          children: [],
        }],
      }],
    }],
  };
}

class FakeStore implements UserSyllabiStore {
  readonly values = new Map<string, { ownerId: string; syllabus: PrivateSyllabus }>();
  readonly mutations = new Map<string, string>();
  readonly calls: string[] = [];

  async list(ownerId: string): Promise<PrivateSyllabus[]> {
    this.calls.push(`list:${ownerId}`);
    return [...this.values.values()].filter((value) => value.ownerId === ownerId).map((value) => value.syllabus);
  }

  async get(ownerId: string, remoteSyllabusId: string): Promise<PrivateSyllabus | null> {
    this.calls.push(`get:${ownerId}:${remoteSyllabusId}`);
    const value = this.values.get(remoteSyllabusId);
    return value?.ownerId === ownerId ? value.syllabus : null;
  }

  async upsert(input: { ownerId: string; mutationId: string; payloadHash: string; syllabus: PrivateSyllabus }): Promise<RemoteSyllabusSyncAcknowledgement> {
    this.calls.push(`upsert:${input.ownerId}:${input.syllabus.remoteSyllabusId}`);
    const existing = this.mutations.get(`${input.ownerId}:${input.mutationId}`);
    if (existing && existing !== input.payloadHash) throw new Error("IDEMPOTENCY_KEY_CONFLICT");
    this.mutations.set(`${input.ownerId}:${input.mutationId}`, input.payloadHash);
    this.values.set(input.syllabus.remoteSyllabusId, { ownerId: input.ownerId, syllabus: input.syllabus });
    return acknowledgement(input.syllabus.remoteSyllabusId, input.payloadHash);
  }

  async delete(input: { ownerId: string; remoteSyllabusId: string; mutationId: string; payloadHash: string }): Promise<RemoteSyllabusSyncAcknowledgement> {
    this.calls.push(`delete:${input.ownerId}:${input.remoteSyllabusId}`);
    const existing = this.mutations.get(`${input.ownerId}:${input.mutationId}`);
    if (existing && existing !== input.payloadHash) throw new Error("IDEMPOTENCY_KEY_CONFLICT");
    this.mutations.set(`${input.ownerId}:${input.mutationId}`, input.payloadHash);
    const value = this.values.get(input.remoteSyllabusId);
    if (value?.ownerId !== input.ownerId) throw new Error("NOT_FOUND");
    this.values.delete(input.remoteSyllabusId);
    return acknowledgement(input.remoteSyllabusId, input.payloadHash);
  }
}

function acknowledgement(remoteSyllabusId: string, payloadHash: string): RemoteSyllabusSyncAcknowledgement {
  return {
    remoteSyllabusId,
    jobId: null,
    payloadHash,
    state: "SYNCED",
    attemptCount: 1,
    nextAttemptAt: null,
    safeError: null,
    createdAt: "2026-09-24T00:00:00Z",
    updatedAt: "2026-09-24T00:00:00Z",
    attemptToken: null,
  };
}

function dependencies(store: FakeStore, userId = USER_A): UserSyllabiDependencies {
  return { authenticate: async () => ({ userId }), store };
}

function request(path: string, init: RequestInit = {}): Request {
  return new Request(`https://example.test${path}`, {
    ...init,
    headers: {
      authorization: "Bearer test-jwt",
      "content-type": "application/json",
      ...(init.headers ?? {}),
    },
  });
}

Deno.test("lists and reads only the authenticated owner's private syllabi", async () => {
  const store = new FakeStore();
  store.values.set("remote-a", { ownerId: USER_A, syllabus: syllabus() });
  store.values.set("remote-b", { ownerId: USER_B, syllabus: syllabus("remote-b") });
  const handler = createUserSyllabiHandler(dependencies(store));

  const list = await handler(request("/user-syllabi"));
  assert.equal(list.status, 200);
  assert.deepEqual((await list.json()).map((item: PrivateSyllabus) => item.remoteSyllabusId), ["remote-a"]);

  const read = await handler(request("/user-syllabi/remote-a"));
  assert.equal(read.status, 200);
  assert.equal((await read.json()).remoteSyllabusId, "remote-a");

  const denied = await handler(request("/user-syllabi/remote-b"));
  assert.equal(denied.status, 404);
});

Deno.test("upserts the complete tree and acknowledges the exact payload hash idempotently", async () => {
  const store = new FakeStore();
  const handler = createUserSyllabiHandler(dependencies(store));
  const body = JSON.stringify(syllabus());
  const headers = { "idempotency-key": "mutation-1" };

  const first = await handler(request("/user-syllabi/remote-a", { method: "PUT", headers, body }));
  assert.equal(first.status, 200);
  const firstAck = await first.json();
  assert.equal(firstAck.state, "SYNCED");
  assert.match(firstAck.payloadHash, /^[0-9a-f]{64}$/);

  const second = await handler(request("/user-syllabi/remote-a", { method: "PUT", headers, body }));
  assert.equal(second.status, 200);
  assert.deepEqual(await second.json(), firstAck);

  const changed = syllabus();
  changed.title = "Outro título";
  const conflict = await handler(request("/user-syllabi/remote-a", { method: "PUT", headers, body: JSON.stringify(changed) }));
  assert.equal(conflict.status, 409);
  assert.equal((await conflict.json()).error.code, "IDEMPOTENCY_KEY_CONFLICT");
});

Deno.test("accepts a private syllabus PUT at the configured request byte limit", async () => {
  const store = new FakeStore();
  const body = JSON.stringify(syllabus());
  const maxPayloadBytes = new TextEncoder().encode(body).byteLength;
  const handler = createUserSyllabiHandler({ ...dependencies(store), maxPayloadBytes });

  const response = await handler(request("/user-syllabi/remote-a", {
    method: "PUT", headers: { "idempotency-key": "at-limit" }, body,
  }));

  assert.equal(response.status, 200);
  assert.deepEqual(store.calls, [`upsert:${USER_A}:remote-a`]);
});

Deno.test("rejects an oversized private syllabus PUT before parsing or storing it", async () => {
  const store = new FakeStore();
  const body = JSON.stringify(syllabus());
  const maxPayloadBytes = new TextEncoder().encode(body).byteLength - 1;
  const handler = createUserSyllabiHandler({ ...dependencies(store), maxPayloadBytes });

  const response = await handler(request("/user-syllabi/remote-a", {
    method: "PUT", headers: { "idempotency-key": "over-limit" }, body,
  }));

  assert.equal(response.status, 413);
  assert.equal((await response.json()).error.code, "PRIVATE_SYLLABUS_TOO_LARGE");
  assert.deepEqual(store.calls, []);
});

Deno.test("rejects invalid parent relationships and cross-account upserts/deletes", async () => {
  const store = new FakeStore();
  const handler = createUserSyllabiHandler(dependencies(store));
  const invalid = syllabus();
  invalid.subjects[0].topics[0].children[0].parentRemoteTopicId = "wrong-parent";
  const invalidResponse = await handler(request("/user-syllabi/remote-a", {
    method: "PUT",
    headers: { "idempotency-key": "invalid-parent" },
    body: JSON.stringify(invalid),
  }));
  assert.equal(invalidResponse.status, 400);
  assert.equal((await invalidResponse.json()).error.code, "INVALID_SYLLABUS");

  store.values.set("remote-b", { ownerId: USER_B, syllabus: syllabus("remote-b") });
  const deleteResponse = await handler(request("/user-syllabi/remote-b", {
    method: "DELETE",
    headers: { "idempotency-key": "delete-b" },
  }));
  assert.equal(deleteResponse.status, 404);
  assert.equal(store.values.get("remote-b")?.ownerId, USER_B);
});
