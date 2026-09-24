import type { JsonSchema } from "./schema.ts";

export type ProviderResponseStatus =
  | "queued"
  | "in_progress"
  | "completed"
  | "failed"
  | "cancelled"
  | "expired"
  | "incomplete";

export interface ProviderUsage {
  inputTokens: number | null;
  outputTokens: number | null;
  totalTokens: number | null;
}

export interface ProviderResponse {
  id: string;
  status: ProviderResponseStatus;
  outputText: string | null;
  usage: ProviderUsage | null;
}

export interface ProviderStartInput {
  jobId: string;
  idempotencyKey: string;
  source: { filename: string; bytes: Uint8Array };
  prompt: string;
  promptVersion: string;
  schemaVersion: number;
  schema: JsonSchema;
  model?: string;
  background?: boolean;
  maxOutputTokens?: number;
}

export interface OpenAiProvider {
  start(input: ProviderStartInput): Promise<ProviderResponse>;
  retrieve(responseId: string): Promise<ProviderResponse>;
  cancel(responseId: string): Promise<ProviderResponse>;
}

export interface OpenAiProviderOptions {
  apiKey?: string;
  baseUrl?: string;
  model?: string;
  background?: boolean;
  timeoutMs?: number;
  fetcher?: typeof fetch;
}

export class OpenAiProviderError extends Error {
  constructor(public readonly code: "OPENAI_API_KEY_MISSING" | "OPENAI_TIMEOUT" | "OPENAI_PROVIDER_ERROR" | "OPENAI_RESPONSE_INVALID", message = code) {
    super(message);
    this.name = "OpenAiProviderError";
  }
}

function environmentValue(name: string): string | undefined {
  try {
    return Deno.env.get(name)?.trim() || undefined;
  } catch {
    return undefined;
  }
}

function environmentBoolean(name: string): boolean {
  return ["1", "true", "yes", "on"].includes((environmentValue(name) ?? "").toLowerCase());
}

function base64(bytes: Uint8Array): string {
  let value = "";
  const chunkSize = 0x8000;
  for (let index = 0; index < bytes.length; index += chunkSize) {
    value += String.fromCharCode(...bytes.subarray(index, index + chunkSize));
  }
  return btoa(value);
}

function numeric(value: unknown): number | null {
  return typeof value === "number" && Number.isSafeInteger(value) && value >= 0 ? value : null;
}

function usage(value: unknown): ProviderUsage | null {
  if (value === null || typeof value !== "object" || Array.isArray(value)) return null;
  const row = value as Record<string, unknown>;
  const inputTokens = numeric(row.input_tokens);
  const outputTokens = numeric(row.output_tokens);
  const totalTokens = numeric(row.total_tokens);
  if (inputTokens === null && outputTokens === null && totalTokens === null) return null;
  return { inputTokens, outputTokens, totalTokens };
}

function outputText(value: unknown): string | null {
  if (typeof value === "string") return value;
  if (!Array.isArray(value)) return null;
  for (const item of value) {
    if (item === null || typeof item !== "object") continue;
    const content = (item as Record<string, unknown>).content;
    if (!Array.isArray(content)) continue;
    for (const part of content) {
      if (part !== null && typeof part === "object" && typeof (part as Record<string, unknown>).text === "string") {
        return (part as Record<string, string>).text;
      }
    }
  }
  return null;
}

function responseStatus(value: unknown): ProviderResponseStatus {
  if (["queued", "in_progress", "completed", "failed", "cancelled", "expired", "incomplete"].includes(String(value))) {
    return value as ProviderResponseStatus;
  }
  throw new OpenAiProviderError("OPENAI_RESPONSE_INVALID");
}

function parseResponse(value: unknown): ProviderResponse {
  if (value === null || typeof value !== "object" || Array.isArray(value)) throw new OpenAiProviderError("OPENAI_RESPONSE_INVALID");
  const row = value as Record<string, unknown>;
  if (typeof row.id !== "string" || row.id.trim().length === 0) throw new OpenAiProviderError("OPENAI_RESPONSE_INVALID");
  return {
    id: row.id,
    status: responseStatus(row.status),
    outputText: outputText(row.output_text ?? row.output),
    usage: usage(row.usage),
  };
}

function openAiCompatibleSchema(value: JsonSchema): JsonSchema {
  const clone = (item: unknown): unknown => {
    if (Array.isArray(item)) return item.map(clone);
    if (item === null || typeof item !== "object") return item;
    const result: Record<string, unknown> = {};
    for (const [key, child] of Object.entries(item as Record<string, unknown>)) {
      if (key === "$schema" || key === "$id" || key === "$comment") continue;
      result[key] = clone(child);
    }
    return result;
  };
  return clone(value) as JsonSchema;
}

export function createOpenAiProvider(options: OpenAiProviderOptions = {}): OpenAiProvider {
  const apiKey = options.apiKey?.trim() || environmentValue("OPENAI_API_KEY");
  const baseUrl = (options.baseUrl ?? "https://api.openai.com/v1").replace(/\/$/, "");
  const model = options.model ?? environmentValue("AI_DEFAULT_MODEL") ?? "gpt-6-luna";
  const background = options.background ?? environmentBoolean("AI_OPENAI_BACKGROUND");
  const timeoutMs = options.timeoutMs ?? 30_000;
  const fetcher = options.fetcher ?? fetch;

  const request = async (path: string, init: RequestInit): Promise<ProviderResponse> => {
    if (!apiKey) throw new OpenAiProviderError("OPENAI_API_KEY_MISSING");
    const controller = new AbortController();
    const timeout = setTimeout(() => controller.abort(), timeoutMs);
    try {
      let response: Response;
      try {
        const fetchPromise = fetcher(`${baseUrl}${path}`, {
          ...init,
          signal: controller.signal,
          headers: {
            authorization: `Bearer ${apiKey}`,
            accept: "application/json",
            "content-type": "application/json",
            ...(init.headers ?? {}),
          },
        });
        const timeoutPromise = new Promise<never>((_, reject) => {
          setTimeout(() => reject(new OpenAiProviderError("OPENAI_TIMEOUT")), timeoutMs);
        });
        response = await Promise.race([fetchPromise, timeoutPromise]);
      } catch (error) {
        if (error instanceof DOMException && error.name === "AbortError") throw new OpenAiProviderError("OPENAI_TIMEOUT");
        if (error instanceof OpenAiProviderError) throw error;
        throw new OpenAiProviderError("OPENAI_PROVIDER_ERROR");
      }
      if (!response.ok) throw new OpenAiProviderError("OPENAI_PROVIDER_ERROR");
      let payload: unknown;
      try {
        payload = await response.json();
      } catch {
        throw new OpenAiProviderError("OPENAI_RESPONSE_INVALID");
      }
      return parseResponse(payload);
    } finally {
      clearTimeout(timeout);
    }
  };

  return {
    start: (input) => request("/responses", {
      method: "POST",
      headers: { "Idempotency-Key": input.idempotencyKey },
      body: JSON.stringify({
        model: input.model ?? model,
        background: input.background ?? background,
        input: [{
          role: "user",
          content: [
            { type: "input_file", filename: input.source.filename, file_data: `data:application/pdf;base64,${base64(input.source.bytes)}` },
            { type: "input_text", text: input.prompt },
          ],
        }],
        text: {
          format: {
            type: "json_schema",
            name: `ai_syllabus_proposal_v${input.schemaVersion}`,
            strict: true,
            schema: openAiCompatibleSchema(input.schema),
          },
        },
        ...(input.maxOutputTokens === undefined ? {} : { max_output_tokens: input.maxOutputTokens }),
      }),
    }),
    retrieve: (responseId) => request(`/responses/${encodeURIComponent(responseId)}`, { method: "GET" }),
    cancel: (responseId) => request(`/responses/${encodeURIComponent(responseId)}/cancel`, { method: "POST", body: "{}" }),
  };
}
