import {
  ContractValidationError,
  parsePrivateSyllabus,
  parseRemoteSyllabusSyncAcknowledgement,
  type PrivateSyllabus,
  type PrivateSyllabusSubject,
  type PrivateSyllabusTopic,
  type RemoteSyllabusSyncAcknowledgement,
} from "../_shared/contracts.ts";
import {
  authenticateSupabaseRequest,
  AuthError,
  type AuthenticatedUser,
} from "../_shared/auth.ts";

export interface UserSyllabiStore {
  list(ownerId: string): Promise<PrivateSyllabus[]>;
  get(ownerId: string, remoteSyllabusId: string): Promise<PrivateSyllabus | null>;
  upsert(input: {
    ownerId: string;
    mutationId: string;
    payloadHash: string;
    syllabus: PrivateSyllabus;
  }): Promise<RemoteSyllabusSyncAcknowledgement>;
  delete(input: {
    ownerId: string;
    remoteSyllabusId: string;
    mutationId: string;
    payloadHash: string;
  }): Promise<RemoteSyllabusSyncAcknowledgement>;
}

export interface UserSyllabiDependencies {
  authenticate: (request: Request) => Promise<AuthenticatedUser>;
  store: UserSyllabiStore;
}

class UserSyllabiStoreError extends Error {
  constructor(public readonly code: string, public readonly status: number) {
    super(code);
    this.name = "UserSyllabiStoreError";
  }
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

function errorResponse(code: string, status: number): Response {
  const messages: Record<string, string> = {
    AUTH_REQUIRED: "Authentication required",
    AUTH_INVALID: "Authentication required",
    INVALID_REQUEST: "Invalid request",
    INVALID_SYLLABUS: "The syllabus tree is invalid",
    IDEMPOTENCY_KEY_REQUIRED: "An idempotency key is required",
    IDEMPOTENCY_KEY_CONFLICT: "The idempotency key was reused with a different payload",
    NOT_FOUND: "Private syllabus not found",
    PRIVATE_SYLLABUS_UNAVAILABLE: "Private syllabus is temporarily unavailable",
  };
  return jsonResponse({ error: { code, message: messages[code] ?? "Private syllabus request could not be completed" } }, status);
}

function isObject(value: unknown): value is Record<string, unknown> {
  return value !== null && typeof value === "object" && !Array.isArray(value);
}

function mutationId(request: Request): string {
  const value = request.headers.get("idempotency-key")?.trim() ?? request.headers.get("x-mutation-id")?.trim() ?? "";
  if (value.length === 0 || value.length > 255) throw new UserSyllabiStoreError("IDEMPOTENCY_KEY_REQUIRED", 400);
  return value;
}

function hexHash(value: string): string {
  if (!/^[0-9a-f]{64}$/i.test(value)) throw new UserSyllabiStoreError("INVALID_REQUEST", 400);
  return value.toLowerCase();
}

async function sha256(value: string): Promise<string> {
  const digest = await crypto.subtle.digest("SHA-256", new TextEncoder().encode(value));
  return [...new Uint8Array(digest)].map((byte) => byte.toString(16).padStart(2, "0")).join("");
}

function stableJson(value: unknown): string {
  if (Array.isArray(value)) return `[${value.map(stableJson).join(",")}]`;
  if (isObject(value)) {
    return `{${Object.keys(value).sort().map((key) => `${JSON.stringify(key)}:${stableJson(value[key])}`).join(",")}}`;
  }
  const serialized = JSON.stringify(value);
  if (serialized === undefined) throw new UserSyllabiStoreError("INVALID_REQUEST", 400);
  return serialized;
}

async function requestPayloadHash(request: Request, value: unknown): Promise<string> {
  const declared = request.headers.get("x-payload-hash")?.trim();
  if (declared) {
    const normalized = hexHash(declared);
    if (isObject(value) && isObject(value.metadata) && typeof value.metadata.payloadHash === "string" && value.metadata.payloadHash.toLowerCase() !== normalized) {
      throw new UserSyllabiStoreError("IDEMPOTENCY_KEY_CONFLICT", 409);
    }
    return normalized;
  }
  return sha256(stableJson(value));
}

async function requestJson(request: Request): Promise<Record<string, unknown>> {
  try {
    const value: unknown = await request.json();
    if (!isObject(value)) throw new Error();
    return value;
  } catch {
    throw new UserSyllabiStoreError("INVALID_REQUEST", 400);
  }
}

function syllabusBody(body: Record<string, unknown>): unknown {
  return isObject(body.syllabus) ? body.syllabus : body;
}

function mapStoreError(error: unknown): Response {
  if (error instanceof UserSyllabiStoreError) return errorResponse(error.code, error.status);
  if (error instanceof ContractValidationError) return errorResponse("INVALID_SYLLABUS", 400);
  if (error instanceof Error && error.message === "IDEMPOTENCY_KEY_CONFLICT") return errorResponse("IDEMPOTENCY_KEY_CONFLICT", 409);
  if (error instanceof Error && error.message === "NOT_FOUND") return errorResponse("NOT_FOUND", 404);
  return errorResponse("PRIVATE_SYLLABUS_UNAVAILABLE", 503);
}

export function createUserSyllabiHandler(dependencies: UserSyllabiDependencies): (request: Request) => Promise<Response> {
  return async (request: Request): Promise<Response> => {
    let user: AuthenticatedUser;
    try {
      user = await dependencies.authenticate(request);
    } catch (error) {
      return error instanceof AuthError ? errorResponse(error.code, error.status) : errorResponse("AUTH_INVALID", 401);
    }

    const pathname = new URL(request.url).pathname.replace(/\/+$/, "") || "/";
    const segments = pathname.split("/").filter(Boolean);
    if (segments[0] !== "user-syllabi" || segments.length > 2) return errorResponse("NOT_FOUND", 404);
    const remoteSyllabusId = segments[1] ?? null;

    try {
      if (request.method === "GET" && remoteSyllabusId === null) return jsonResponse(await dependencies.store.list(user.userId));
      if (request.method === "GET" && remoteSyllabusId !== null) {
        const value = await dependencies.store.get(user.userId, remoteSyllabusId);
        return value ? jsonResponse(value) : errorResponse("NOT_FOUND", 404);
      }
      if (request.method === "PUT" && remoteSyllabusId !== null) {
        const id = mutationId(request);
        const body = await requestJson(request);
        const rawSyllabus = syllabusBody(body);
        const syllabus = parsePrivateSyllabus(rawSyllabus);
        if (syllabus.remoteSyllabusId !== remoteSyllabusId) throw new UserSyllabiStoreError("INVALID_SYLLABUS", 400);
        const payloadHash = await requestPayloadHash(request, rawSyllabus);
        const acknowledgement = await dependencies.store.upsert({ ownerId: user.userId, mutationId: id, payloadHash, syllabus });
        return jsonResponse(acknowledgement);
      }
      if (request.method === "DELETE" && remoteSyllabusId !== null) {
        const id = mutationId(request);
        const payloadHash = await requestPayloadHash(request, { operation: "DELETE", remoteSyllabusId });
        return jsonResponse(await dependencies.store.delete({ ownerId: user.userId, remoteSyllabusId, mutationId: id, payloadHash }));
      }
      return errorResponse("INVALID_REQUEST", 405);
    } catch (error) {
      return mapStoreError(error);
    }
  };
}

interface SupabasePrivateSyllabusEnvironment {
  supabaseUrl: string;
  serviceRoleKey: string;
  fetcher?: typeof fetch;
}

export class SupabasePrivateSyllabusStore implements UserSyllabiStore {
  private readonly fetcher: typeof fetch;

  constructor(private readonly environment: SupabasePrivateSyllabusEnvironment) {
    this.fetcher = environment.fetcher ?? fetch;
  }

  async list(ownerId: string): Promise<PrivateSyllabus[]> {
    const roots = await this.select("user_syllabi", {
      owner_user_id: `eq.${ownerId}`,
      status: "neq.DELETED",
      order: "position.asc,created_at.asc",
    });
    return Promise.all(roots.map((root) => this.readTree(root)));
  }

  async get(ownerId: string, remoteSyllabusId: string): Promise<PrivateSyllabus | null> {
    const rows = await this.select("user_syllabi", { id: `eq.${remoteSyllabusId}`, owner_user_id: `eq.${ownerId}`, limit: "1" });
    return rows.length === 0 ? null : this.readTree(rows[0]);
  }

  async upsert(input: { ownerId: string; mutationId: string; payloadHash: string; syllabus: PrivateSyllabus }): Promise<RemoteSyllabusSyncAcknowledgement> {
    const existingOwner = await this.select("user_syllabi", { id: `eq.${input.syllabus.remoteSyllabusId}`, select: "owner_user_id", limit: "1" });
    if (existingOwner.length > 0 && stringField(existingOwner[0], "owner_user_id") !== input.ownerId) throw new UserSyllabiStoreError("NOT_FOUND", 404);
    const mutation = await this.findMutation(input.ownerId, input.mutationId);
    if (mutation && stringField(mutation, "payload_hash") !== input.payloadHash) {
      throw new UserSyllabiStoreError("IDEMPOTENCY_KEY_CONFLICT", 409);
    }
    if (mutation?.response !== null && mutation?.response !== undefined) return parseRemoteSyllabusSyncAcknowledgement(mutation.response);

    await this.write("user_syllabi", {
      id: input.syllabus.remoteSyllabusId,
      owner_user_id: input.ownerId,
      title: input.syllabus.title,
      position: input.syllabus.position,
      visibility: "PRIVATE",
      source: input.syllabus.source,
      source_job_id: uuidOrNull(input.syllabus.sourceJobId),
      source_hash: input.syllabus.sourceHash,
      schema_version: input.syllabus.schemaVersion,
      status: input.syllabus.status,
      metadata: input.syllabus.metadata,
    }, "merge-duplicates");
    await this.deleteChildren(input.syllabus.remoteSyllabusId);
    for (const subject of input.syllabus.subjects) {
      await this.write("user_syllabus_subjects", {
        id: subject.remoteSubjectId,
        syllabus_id: input.syllabus.remoteSyllabusId,
        external_id: subject.externalId,
        name: subject.name,
        position: subject.position,
        suggested_priority: subject.suggestedPriority,
        package_version: subject.packageVersion,
        schema_version: subject.schemaVersion,
        metadata: subject.metadata,
      });
      await this.writeTopics(input.syllabus.remoteSyllabusId, subject.remoteSubjectId, subject.topics);
    }
    const response = acknowledgement(input.syllabus.remoteSyllabusId, input.payloadHash);
    await this.recordMutation(input.ownerId, input.mutationId, input.syllabus.remoteSyllabusId, "UPSERT", input.payloadHash, response);
    return response;
  }

  async delete(input: { ownerId: string; remoteSyllabusId: string; mutationId: string; payloadHash: string }): Promise<RemoteSyllabusSyncAcknowledgement> {
    const mutation = await this.findMutation(input.ownerId, input.mutationId);
    if (mutation && stringField(mutation, "payload_hash") !== input.payloadHash) {
      throw new UserSyllabiStoreError("IDEMPOTENCY_KEY_CONFLICT", 409);
    }
    if (mutation?.response !== null && mutation?.response !== undefined) return parseRemoteSyllabusSyncAcknowledgement(mutation.response);
    const existing = await this.get(input.ownerId, input.remoteSyllabusId);
    if (!existing) throw new UserSyllabiStoreError("NOT_FOUND", 404);
    const response = await this.fetch(this.restUrl("user_syllabi", { id: `eq.${input.remoteSyllabusId}`, owner_user_id: `eq.${input.ownerId}` }), {
      method: "DELETE",
      headers: this.headers(),
    });
    if (!response.ok) throw await this.storeError(response);
    const responseBody = acknowledgement(input.remoteSyllabusId, input.payloadHash);
    await this.recordMutation(input.ownerId, input.mutationId, input.remoteSyllabusId, "DELETE", input.payloadHash, responseBody);
    return responseBody;
  }

  private async readTree(root: Record<string, unknown>): Promise<PrivateSyllabus> {
    const syllabusId = stringField(root, "id");
    const subjects = await this.select("user_syllabus_subjects", { syllabus_id: `eq.${syllabusId}`, order: "position.asc" });
    const result = {
      remoteSyllabusId: syllabusId,
      title: stringField(root, "title"),
      position: integerField(root, "position"),
      visibility: "PRIVATE" as const,
      source: stringField(root, "source") as PrivateSyllabus["source"],
      sourceJobId: nullableString(root, "source_job_id"),
      sourceHash: nullableString(root, "source_hash"),
      schemaVersion: integerField(root, "schema_version"),
      status: stringField(root, "status") as PrivateSyllabus["status"],
      metadata: objectField(root, "metadata"),
      subjects: await Promise.all(subjects.map(async (subject) => ({
        remoteSubjectId: stringField(subject, "id"),
        externalId: stringField(subject, "external_id"),
        name: stringField(subject, "name"),
        position: integerField(subject, "position"),
        suggestedPriority: stringField(subject, "suggested_priority") as PrivateSyllabusSubject["suggestedPriority"],
        packageVersion: stringField(subject, "package_version"),
        schemaVersion: integerField(subject, "schema_version"),
        metadata: objectField(subject, "metadata"),
        topics: await this.readTopics(stringField(subject, "id")),
      }))),
    } satisfies PrivateSyllabus;
    return parsePrivateSyllabus(result);
  }

  private async readTopics(subjectId: string): Promise<PrivateSyllabusTopic[]> {
    const rows = await this.select("user_syllabus_topics", { subject_id: `eq.${subjectId}`, order: "position.asc" });
    const byParent = new Map<string | null, Record<string, unknown>[]>();
    for (const row of rows) {
      const parent = nullableString(row, "parent_topic_id");
      const siblings = byParent.get(parent) ?? [];
      siblings.push(row);
      byParent.set(parent, siblings);
    }
    const map = (row: Record<string, unknown>): PrivateSyllabusTopic => {
      const id = stringField(row, "id");
      return {
        remoteTopicId: id,
        externalId: stringField(row, "external_id"),
        parentRemoteTopicId: nullableString(row, "parent_topic_id"),
        name: stringField(row, "name"),
        position: integerField(row, "position"),
        packageVersion: stringField(row, "package_version"),
        schemaVersion: integerField(row, "schema_version"),
        metadata: objectField(row, "metadata"),
        children: (byParent.get(id) ?? []).map(map),
      };
    };
    return (byParent.get(null) ?? []).map(map);
  }

  private async writeTopics(syllabusId: string, subjectId: string, topics: PrivateSyllabusTopic[]): Promise<void> {
    for (const topic of topics) {
      await this.write("user_syllabus_topics", {
        id: topic.remoteTopicId,
        subject_id: subjectId,
        external_id: topic.externalId,
        parent_topic_id: topic.parentRemoteTopicId,
        name: topic.name,
        position: topic.position,
        package_version: topic.packageVersion,
        schema_version: topic.schemaVersion,
        metadata: topic.metadata,
      });
      await this.writeTopics(syllabusId, subjectId, topic.children);
    }
  }

  private async deleteChildren(syllabusId: string): Promise<void> {
    const subjects = await this.select("user_syllabus_subjects", { syllabus_id: `eq.${syllabusId}`, select: "id" });
    for (const subject of subjects) {
      const response = await this.fetch(this.restUrl("user_syllabus_subjects", { id: `eq.${stringField(subject, "id")}` }), { method: "DELETE", headers: this.headers() });
      if (!response.ok) throw await this.storeError(response);
    }
  }

  private async findMutation(ownerId: string, mutationId: string): Promise<{ payload_hash: string; response: Record<string, unknown> | null } | null> {
    const rows = await this.select("user_syllabus_mutations", { owner_user_id: `eq.${ownerId}`, mutation_id: `eq.${mutationId}`, limit: "1" });
    if (rows.length === 0) return null;
    return {
      payload_hash: stringField(rows[0], "payload_hash"),
      response: rows[0].response === null || rows[0].response === undefined ? null : objectField(rows[0], "response"),
    };
  }

  private async recordMutation(ownerId: string, mutationId: string, remoteSyllabusId: string, operation: "UPSERT" | "DELETE", payloadHash: string, response: RemoteSyllabusSyncAcknowledgement): Promise<void> {
    await this.write("user_syllabus_mutations", {
      owner_user_id: ownerId,
      mutation_id: mutationId,
      remote_syllabus_id: remoteSyllabusId,
      operation,
      payload_hash: payloadHash,
      response,
    }, "merge-duplicates");
  }

  private async select(table: string, filters: Record<string, string>): Promise<Record<string, unknown>[]> {
    const response = await this.fetch(this.restUrl(table, filters), { headers: this.headers() });
    if (!response.ok) throw await this.storeError(response);
    const value: unknown = await response.json();
    if (!Array.isArray(value)) throw new UserSyllabiStoreError("PRIVATE_SYLLABUS_UNAVAILABLE", 503);
    return value.filter(isObject);
  }

  private async write(table: string, body: Record<string, unknown>, resolution?: string): Promise<void> {
    const response = await this.fetch(this.restUrl(table), {
      method: "POST",
      headers: { ...this.headers(), prefer: `resolution=${resolution ?? "error"},return=minimal`, "content-type": "application/json" },
      body: JSON.stringify(body),
    });
    if (!response.ok) throw await this.storeError(response);
  }

  private restUrl(table: string, filters: Record<string, string> = {}): string {
    const url = new URL(`${this.environment.supabaseUrl.replace(/\/$/, "")}/rest/v1/${table}`);
    for (const [key, value] of Object.entries(filters)) url.searchParams.set(key, value);
    return url.toString();
  }

  private headers(): HeadersInit {
    return { apikey: this.environment.serviceRoleKey, authorization: `Bearer ${this.environment.serviceRoleKey}`, accept: "application/json" };
  }

  private fetch(input: RequestInfo | URL, init?: RequestInit): Promise<Response> {
    return this.fetcher(input, init);
  }

  private async storeError(response: Response): Promise<UserSyllabiStoreError> {
    let message = "";
    try { message = String((await response.json() as Record<string, unknown>).message ?? ""); } catch { /* safe fallback */ }
    const code = message === "IDEMPOTENCY_KEY_CONFLICT" ? message : response.status === 404 ? "NOT_FOUND" : "PRIVATE_SYLLABUS_UNAVAILABLE";
    return new UserSyllabiStoreError(code, code === "NOT_FOUND" ? 404 : response.status >= 400 && response.status < 500 ? response.status : 503);
  }
}

function acknowledgement(remoteSyllabusId: string, payloadHash: string): RemoteSyllabusSyncAcknowledgement {
  const now = new Date().toISOString();
  return { remoteSyllabusId, jobId: null, payloadHash, state: "SYNCED", attemptCount: 1, nextAttemptAt: null, safeError: null, createdAt: now, updatedAt: now, attemptToken: null };
}

function uuidOrNull(value: string | null): string | null {
  return value && /^[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$/i.test(value) ? value : null;
}

function stringField(row: Record<string, unknown>, key: string): string {
  const value = row[key];
  if (typeof value !== "string" || value.trim().length === 0) throw new UserSyllabiStoreError("PRIVATE_SYLLABUS_UNAVAILABLE", 503);
  return value;
}

function nullableString(row: Record<string, unknown>, key: string): string | null {
  return row[key] === null || row[key] === undefined ? null : stringField(row, key);
}

function integerField(row: Record<string, unknown>, key: string): number {
  if (typeof row[key] !== "number" || !Number.isInteger(row[key])) throw new UserSyllabiStoreError("PRIVATE_SYLLABUS_UNAVAILABLE", 503);
  return row[key] as number;
}

function objectField(row: Record<string, unknown>, key: string): Record<string, unknown> {
  if (!isObject(row[key])) throw new UserSyllabiStoreError("PRIVATE_SYLLABUS_UNAVAILABLE", 503);
  return row[key];
}

function runtimeEnvironment(): SupabasePrivateSyllabusEnvironment {
  const supabaseUrl = Deno.env.get("SUPABASE_URL")?.trim();
  const serviceRoleKey = Deno.env.get("SUPABASE_SERVICE_ROLE_KEY")?.trim();
  if (!supabaseUrl || !serviceRoleKey) throw new UserSyllabiStoreError("PRIVATE_SYLLABUS_UNAVAILABLE", 503);
  return { supabaseUrl, serviceRoleKey };
}

async function handleUserSyllabi(request: Request): Promise<Response> {
  const store = new SupabasePrivateSyllabusStore(runtimeEnvironment());
  return createUserSyllabiHandler({ authenticate: authenticateSupabaseRequest, store })(request);
}

if (import.meta.main) Deno.serve(handleUserSyllabi);
