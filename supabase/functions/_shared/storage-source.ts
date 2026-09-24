export const AI_SYLLABUS_SOURCE_BUCKET = "ai-syllabus-sources";

export interface StorageSourceLimits {
  maxBytes: number;
  maxPages: number;
  maxFiles: number;
}

export interface StorageObject {
  bucketId: string;
  path: string;
  ownerId: string | null;
  mimeType: string | null;
  sizeBytes: number | null;
  metadata: Record<string, unknown>;
  body: Uint8Array;
}

export interface StorageSourceStore {
  getObject(userId: string, path: string): Promise<StorageObject | null>;
}

export interface BoundStorageSource {
  path: string;
  mimeType: string;
  sourceHash: string;
  sourceBytes: number;
  sourcePages: number;
  sourceFileCount: number;
  metadata: Record<string, unknown>;
}

export class StorageSourceError extends Error {
  constructor(
    public readonly code:
    | "SOURCE_PATH_INVALID"
    | "SOURCE_NOT_FOUND"
    | "SOURCE_METADATA_INVALID"
    | "SOURCE_MIME_UNSUPPORTED"
    | "SOURCE_TOO_LARGE"
    | "SOURCE_TOO_MANY_PAGES"
    | "SOURCE_TOO_MANY_FILES"
    | "SOURCE_PAGE_COUNT_UNAVAILABLE",
    public readonly status: 400 | 404 | 413 | 415 | 422,
  ) {
    super(code);
    this.name = "StorageSourceError";
  }
}

function validUserScopedPath(userId: string, path: string): boolean {
  if (path.includes("\\") || path.includes("..") || path.startsWith("/")) return false;
  const parts = path.split("/");
  return parts.length === 2 && parts[0] === userId && /^[A-Za-z0-9][A-Za-z0-9._-]{0,127}\.pdf$/i.test(parts[1]);
}

function metadataString(metadata: Record<string, unknown>, ...keys: string[]): string | null {
  for (const key of keys) {
    const value = metadata[key];
    if (typeof value === "string" && value.trim().length > 0) return value.trim();
  }
  return null;
}

function metadataBytes(metadata: Record<string, unknown>): number | null {
  const value = metadata.size ?? metadata.sizeBytes ?? metadata.contentLength;
  if (typeof value === "number" && Number.isInteger(value) && value >= 0) return value;
  if (typeof value === "string" && /^\d+$/.test(value)) return Number(value);
  return null;
}

function countPdfPages(body: Uint8Array): number {
  const text = new TextDecoder("latin1").decode(body);
  return (text.match(/\/Type\s*\/Page(?!s)(?:\s|\/|>)/g) ?? []).length;
}

function hexDigest(bytes: ArrayBuffer): string {
  return [...new Uint8Array(bytes)].map((byte) => byte.toString(16).padStart(2, "0")).join("");
}

export async function validateAndBindStorageSource(
  userId: string,
  path: string,
  store: StorageSourceStore,
  limits: StorageSourceLimits,
  clientMimeType?: string | null,
): Promise<BoundStorageSource> {
  if (!validUserScopedPath(userId, path)) throw new StorageSourceError("SOURCE_PATH_INVALID", 400);
  if (limits.maxFiles < 1) throw new StorageSourceError("SOURCE_TOO_MANY_FILES", 413);

  const object = await store.getObject(userId, path);
  if (!object) throw new StorageSourceError("SOURCE_NOT_FOUND", 404);
  if (object.bucketId !== AI_SYLLABUS_SOURCE_BUCKET || object.path !== path) {
    throw new StorageSourceError("SOURCE_METADATA_INVALID", 422);
  }
  if (object.ownerId !== null && object.ownerId !== userId) {
    throw new StorageSourceError("SOURCE_METADATA_INVALID", 422);
  }

  const metadataOwner = metadataString(object.metadata, "ownerId", "owner_id", "userId", "user_id");
  if (metadataOwner !== null && metadataOwner !== userId) {
    throw new StorageSourceError("SOURCE_METADATA_INVALID", 422);
  }

  const mimeType = object.mimeType?.split(";", 1)[0].trim().toLowerCase() ?? "";
  const metadataMime = metadataString(object.metadata, "mimetype", "mimeType", "contentType", "content-type")
    ?.split(";", 1)[0].trim().toLowerCase();
  if (clientMimeType && clientMimeType.split(";", 1)[0].trim().toLowerCase() !== "application/pdf") {
    throw new StorageSourceError("SOURCE_MIME_UNSUPPORTED", 415);
  }
  if (mimeType !== "application/pdf" || (metadataMime !== null && metadataMime !== "application/pdf")) {
    throw new StorageSourceError("SOURCE_MIME_UNSUPPORTED", 415);
  }
  if (object.sizeBytes !== null && object.sizeBytes !== object.body.byteLength) {
    throw new StorageSourceError("SOURCE_METADATA_INVALID", 422);
  }
  const declaredBytes = metadataBytes(object.metadata);
  if (declaredBytes !== null && declaredBytes !== object.body.byteLength) {
    throw new StorageSourceError("SOURCE_METADATA_INVALID", 422);
  }
  if (object.body.byteLength > limits.maxBytes) throw new StorageSourceError("SOURCE_TOO_LARGE", 413);

  const pages = countPdfPages(object.body);
  if (pages < 1) throw new StorageSourceError("SOURCE_PAGE_COUNT_UNAVAILABLE", 422);
  if (pages > limits.maxPages) throw new StorageSourceError("SOURCE_TOO_MANY_PAGES", 413);

  const digestInput = new ArrayBuffer(object.body.byteLength);
  new Uint8Array(digestInput).set(object.body);
  const sourceHash = hexDigest(await crypto.subtle.digest("SHA-256", digestInput));
  return {
    path,
    mimeType: "application/pdf",
    sourceHash,
    sourceBytes: object.body.byteLength,
    sourcePages: pages,
    sourceFileCount: 1,
    metadata: {
      bucket: object.bucketId,
      path: object.path,
      mimeType: "application/pdf",
      sizeBytes: object.body.byteLength,
      storageMetadata: object.metadata,
    },
  };
}

export interface SupabaseStorageEnvironment {
  supabaseUrl: string;
  serviceRoleKey: string;
  fetcher?: typeof fetch;
}

export class SupabaseStorageSourceStore implements StorageSourceStore {
  constructor(
    private readonly environment: SupabaseStorageEnvironment,
    private readonly maxBytes: number,
  ) {}

  async getObject(userId: string, path: string): Promise<StorageObject | null> {
    const fetcher = this.environment.fetcher ?? fetch;
    const baseUrl = this.environment.supabaseUrl.replace(/\/$/, "");
    const metadataUrl = new URL(`${baseUrl}/rest/v1/storage.objects`);
    metadataUrl.searchParams.set("select", "bucket_id,name,owner,metadata");
    metadataUrl.searchParams.set("bucket_id", `eq.${AI_SYLLABUS_SOURCE_BUCKET}`);
    metadataUrl.searchParams.set("name", `eq.${path}`);
    const metadataResponse = await fetcher(metadataUrl, { headers: this.headers() });
    if (!metadataResponse.ok) throw new StorageSourceError("SOURCE_METADATA_INVALID", 422);
    const rows = await metadataResponse.json() as unknown;
    if (!Array.isArray(rows) || rows.length === 0) return null;
    const row = rows[0] as Record<string, unknown>;
    const metadata = row.metadata && typeof row.metadata === "object" && !Array.isArray(row.metadata)
      ? row.metadata as Record<string, unknown>
      : {};
    const owner = typeof row.owner === "string" ? row.owner : null;
    const mimeType = metadataString(metadata, "mimetype", "mimeType", "contentType");
    const bodyResponse = await fetcher(
      `${baseUrl}/storage/v1/object/${AI_SYLLABUS_SOURCE_BUCKET}/${path}`,
      { headers: this.headers() },
    );
    if (bodyResponse.status === 404) return null;
    if (!bodyResponse.ok) throw new StorageSourceError("SOURCE_NOT_FOUND", 404);
    const body = await readBodyWithinLimit(bodyResponse, this.maxBytes);
    return {
      bucketId: typeof row.bucket_id === "string" ? row.bucket_id : "",
      path: typeof row.name === "string" ? row.name : "",
      ownerId: owner,
      mimeType: mimeType ?? bodyResponse.headers.get("content-type"),
      sizeBytes: bodyResponse.headers.has("content-length")
        ? Number(bodyResponse.headers.get("content-length"))
        : null,
      metadata,
      body,
    };
  }

  private headers(): HeadersInit {
    return {
      apikey: this.environment.serviceRoleKey,
      authorization: `Bearer ${this.environment.serviceRoleKey}`,
      accept: "application/json",
    };
  }
}

async function readBodyWithinLimit(response: Response, maxBytes: number): Promise<Uint8Array> {
  const contentLength = response.headers.get("content-length");
  if (contentLength !== null && Number(contentLength) > maxBytes) {
    throw new StorageSourceError("SOURCE_TOO_LARGE", 413);
  }
  if (!response.body) {
    const body = new Uint8Array(await response.arrayBuffer());
    if (body.byteLength > maxBytes) throw new StorageSourceError("SOURCE_TOO_LARGE", 413);
    return body;
  }
  const reader = response.body.getReader();
  const chunks: Uint8Array[] = [];
  let total = 0;
  while (true) {
    const result = await reader.read();
    if (result.done) break;
    total += result.value.byteLength;
    if (total > maxBytes) {
      await reader.cancel();
      throw new StorageSourceError("SOURCE_TOO_LARGE", 413);
    }
    chunks.push(result.value);
  }
  const body = new Uint8Array(total);
  let offset = 0;
  for (const chunk of chunks) {
    body.set(chunk, offset);
    offset += chunk.byteLength;
  }
  return body;
}
