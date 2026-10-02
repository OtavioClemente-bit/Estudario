import type { ContentJobInput } from "./text-job-input.ts";

// Reaproveitamento de material entre pessoas: o mesmo tópico, do mesmo concurso, com as mesmas
// opções, sai do banco em vez de ir de novo à IA. A chave só usa o pedido, nunca quem pediu.

/** Material guardado vale por este tempo; depois, nova geração (lei muda, fonte some). */
export const CACHE_MAX_AGE_DAYS = 45;

const normal = (value: string | null | undefined) =>
  (value ?? "").normalize("NFD").replace(/[̀-ͯ]/g, "").replace(/[º°ª]/g, "").toLowerCase().replace(/\s+/g, " ").trim();

export async function contentCacheKey(input: ContentJobInput): Promise<string> {
  const o = input.options;
  const canonical = JSON.stringify([
    normal(input.competitionName),
    normal(input.role),
    normal(input.board),
    normal(input.agency),
    normal(input.sphere),
    normal(input.subjectName),
    input.topicPath.map(normal),
    normal(input.scopeCovers),
    normal(input.scopeExcludes),
    [...o.blocks].sort(),
    o.depth,
    o.questionCount,
    o.questionStyle,
    o.difficulty,
  ]);
  const digest = await crypto.subtle.digest("SHA-256", new TextEncoder().encode(canonical));
  return Array.from(new Uint8Array(digest), (byte) => byte.toString(16).padStart(2, "0")).join("");
}

export interface ContentCacheStore {
  /** Material guardado e recente para a chave, se a pessoa ainda não o recebeu. */
  cachedContent?(key: string, promptVersion: string, userId: string, maxAgeDays: number): Promise<Record<string, unknown> | null>;
  /** Guarda (ou troca) o material da chave e marca que a pessoa o recebeu. */
  storeContent?(key: string, promptVersion: string, userId: string, jobId: string, proposal: Record<string, unknown>): Promise<void>;
  /** Marca que a pessoa recebeu o material guardado. */
  markServed?(key: string, userId: string): Promise<void>;
}
