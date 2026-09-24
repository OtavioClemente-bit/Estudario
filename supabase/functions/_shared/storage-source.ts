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

function maskPdfNonStructuralRegions(text: string): string | null {
  const chars = [...text];
  const mask = (start: number, end: number) => {
    for (let index = start; index < end; index += 1) {
      if (chars[index] !== "\n" && chars[index] !== "\r") chars[index] = " ";
    }
  };

  let index = 0;
  while (index < chars.length) {
    if (chars[index] === "%") {
      const start = index;
      while (index < chars.length && chars[index] !== "\n" && chars[index] !== "\r") index += 1;
      mask(start, index);
      continue;
    }
    if (chars[index] === "(") {
      const start = index;
      let depth = 1;
      index += 1;
      while (index < chars.length && depth > 0) {
        if (chars[index] === "\\") {
          index += 2;
          continue;
        }
        if (chars[index] === "(") depth += 1;
        if (chars[index] === ")") depth -= 1;
        index += 1;
      }
      if (depth !== 0) return null;
      mask(start, index);
      continue;
    }
    if (chars[index] === "<" && chars[index + 1] === "<") {
      index += 2;
      continue;
    }
    if (chars[index] === "<") {
      const start = index;
      index += 1;
      while (index < chars.length && chars[index] !== ">") index += 1;
      if (index >= chars.length) return null;
      index += 1;
      mask(start, index);
      continue;
    }
    index += 1;
  }

  const masked = chars.join("");
  const streamPattern = /(^|\s)stream(?:\r\n|\n|\r)/g;
  let streamMatch: RegExpExecArray | null;
  while ((streamMatch = streamPattern.exec(masked)) !== null) {
    const dataStart = streamMatch.index + streamMatch[0].length;
    const endStream = masked.indexOf("endstream", dataStart);
    if (endStream < 0) return null;
    mask(dataStart, endStream + "endstream".length);
    streamPattern.lastIndex = endStream + "endstream".length;
  }
  return chars.join("");
}

function countPdfPages(body: Uint8Array): number {
  const text = new TextDecoder("latin1").decode(body);
  if (!/^%PDF-\d\.\d(?:\s|$)/.test(text) || !/%%EOF\s*$/.test(text)) return 0;
  const masked = maskPdfNonStructuralRegions(text);
  if (masked === null) return 0;
  const objects = [...masked.matchAll(/\b\d+\s+\d+\s+obj\b([\s\S]*?)\bendobj\b/g)];
  const objectStarts = masked.match(/\b\d+\s+\d+\s+obj\b/g) ?? [];
  const objectEnds = masked.match(/\bendobj\b/g) ?? [];
  if (objects.length === 0 || objectStarts.length !== objects.length || objectEnds.length !== objects.length) return 0;
  return objects.filter((match) => /\/Type\s*\/Page(?=\s|\/|>>)/.test(match[1])).length;
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
  if (object.ownerId === null || object.ownerId !== userId) {
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
  publishableKey: string;
  accessToken: string;
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
    const metadataResponse = await fetcher(`${baseUrl}/rest/v1/rpc/get_ai_syllabus_source_metadata`, {
      method: "POST",
      headers: { ...this.headers(), "content-type": "application/json" },
      body: JSON.stringify({ p_path: path }),
    });
    if (!metadataResponse.ok) throw new StorageSourceError("SOURCE_METADATA_INVALID", 422);
    const payload = await metadataResponse.json() as unknown;
    const row = Array.isArray(payload) ? payload[0] as Record<string, unknown> : payload as Record<string, unknown>;
    if (!row || typeof row !== "object") return null;
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
        : metadataBytes(metadata),
      metadata,
      body,
    };
  }

  private headers(): HeadersInit {
    return {
      apikey: this.environment.publishableKey,
      authorization: `Bearer ${this.environment.accessToken}`,
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
