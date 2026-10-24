// Quais tópicos do edital da pessoa têm matéria pronta na biblioteca.
//
// POST /library-ready  { items: [{ id, subjectName, topicPath: [...] }] }  ->  { ready: [id, ...] }
//   O plano do Grátis só inclui tópicos prontos (o Grátis não usa IA); o app e o site perguntam aqui
//   e deixam os outros fora do plano. Quando a matéria é publicada, o tópico passa a vir pronto e
//   entra sozinho no próximo replanejamento. A regra é a mesma do gerador: apelido direto do tópico
//   ou rota completa já guardada (lookupAliases). Só leitura; precisa de conta.
import { withCors } from "../_shared/cors.ts";
import { authenticateSupabaseRequest, AuthError } from "../_shared/auth.ts";
import { lookupAliases } from "../_shared/library.ts";

const MAX_ITEMS = 800;

function json(body: unknown, status = 200): Response {
  return new Response(JSON.stringify(body), { status, headers: { "content-type": "application/json; charset=utf-8", "cache-control": "no-store" } });
}

interface Item { id: string; aliases: string[] }

/** Os apelidos de cada tópico pedido (a mesma conta do gerador). */
export function itemsOf(body: unknown): Item[] {
  const raw = (body as { items?: unknown } | null)?.items;
  if (!Array.isArray(raw)) return [];
  return raw.slice(0, MAX_ITEMS).flatMap((entry) => {
    const e = entry as { id?: unknown; subjectName?: unknown; topicPath?: unknown };
    if ((typeof e.id !== "string" && typeof e.id !== "number") || !Array.isArray(e.topicPath)) return [];
    const topicPath = e.topicPath.filter((part): part is string => typeof part === "string").slice(-6).map((part) => part.slice(0, 400));
    if (topicPath.length === 0) return [];
    const aliases = lookupAliases({ topicPath, subjectName: typeof e.subjectName === "string" ? e.subjectName.slice(0, 300) : null });
    return aliases.length ? [{ id: String(e.id), aliases }] : [];
  });
}

async function handle(request: Request): Promise<Response> {
  if (request.method !== "POST") return json({ error: { code: "METHOD_NOT_ALLOWED" } }, 405);
  try {
    await authenticateSupabaseRequest(request);
  } catch (error) {
    return json({ error: { code: error instanceof AuthError ? error.code : "AUTH_UNAVAILABLE" } }, error instanceof AuthError ? error.status : 503);
  }
  const items = itemsOf(await request.json().catch(() => null));
  if (items.length === 0) return json({ ready: [] });
  const url = Deno.env.get("SUPABASE_URL")?.trim(), key = Deno.env.get("SUPABASE_SERVICE_ROLE_KEY")?.trim();
  if (!url || !key) return json({ error: { code: "UNAVAILABLE" } }, 503);
  const norms = [...new Set(items.flatMap((item) => item.aliases))];
  const response = await fetch(`${url.replace(/\/$/, "")}/rest/v1/rpc/library_ready_norms`, {
    method: "POST",
    headers: { apikey: key, authorization: `Bearer ${key}`, "content-type": "application/json", accept: "application/json" },
    body: JSON.stringify({ p_norms: norms }),
  });
  if (!response.ok) return json({ error: { code: "UNAVAILABLE" } }, 503);
  const found = new Set((await response.json() as unknown[]).map((row) => typeof row === "string" ? row : String((row as Record<string, unknown>).library_ready_norms ?? "")));
  return json({ ready: items.filter((item) => item.aliases.some((alias) => found.has(alias))).map((item) => item.id) });
}

if (import.meta.main) Deno.serve(withCors(handle));
