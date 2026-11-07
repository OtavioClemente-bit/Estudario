// Quanto cada tópico do edital da pessoa cai nas provas de verdade.
//
// POST /library-incidence  { board?: "Cebraspe", items: [{ id, subjectName, topicPath: [...] }] }
//   ->  { incidence: [{ id, board, all }] }
//   board = questões oficiais da banca pedida; all = de todas as bancas. Tópico sem matéria na
//   biblioteca não volta. O plano usa para dar mais treino ao que mais cai (e deixar de fora primeiro
//   o que quase nunca cai, quando o edital não cabe). Só leitura; precisa de conta.
import { withCors } from "../_shared/cors.ts";
import { authenticateSupabaseRequest, AuthError } from "../_shared/auth.ts";
import { normalizeBoard } from "../_shared/library.ts";
import { itemsOf } from "../library-ready/index.ts";

function json(body: unknown, status = 200): Response {
  return new Response(JSON.stringify(body), { status, headers: { "content-type": "application/json; charset=utf-8", "cache-control": "no-store" } });
}

async function handle(request: Request): Promise<Response> {
  if (request.method !== "POST") return json({ error: { code: "METHOD_NOT_ALLOWED" } }, 405);
  try {
    await authenticateSupabaseRequest(request);
  } catch (error) {
    return json({ error: { code: error instanceof AuthError ? error.code : "AUTH_UNAVAILABLE" } }, error instanceof AuthError ? error.status : 503);
  }
  const body = await request.json().catch(() => null);
  const items = itemsOf(body);
  if (items.length === 0) return json({ incidence: [] });
  const rawBoard = (body as { board?: unknown } | null)?.board;
  const board = typeof rawBoard === "string" ? normalizeBoard(rawBoard.slice(0, 120)) : "";
  const url = Deno.env.get("SUPABASE_URL")?.trim(), key = Deno.env.get("SUPABASE_SERVICE_ROLE_KEY")?.trim();
  if (!url || !key) return json({ error: { code: "UNAVAILABLE" } }, 503);
  const norms = [...new Set(items.flatMap((item) => item.aliases))];
  const response = await fetch(`${url.replace(/\/$/, "")}/rest/v1/rpc/library_incidence_norms`, {
    method: "POST",
    headers: { apikey: key, authorization: `Bearer ${key}`, "content-type": "application/json", accept: "application/json" },
    body: JSON.stringify({ p_norms: norms, p_board: board }),
  });
  if (!response.ok) return json({ error: { code: "UNAVAILABLE" } }, 503);
  const rows = await response.json() as { norm: string; board_count: number; all_count: number }[];
  const byNorm = new Map(rows.map((row) => [row.norm, row]));
  // O apelido mais específico que achou matéria decide (a ordem dos apelidos é do mais específico ao mais geral).
  const incidence = items.flatMap((item) => {
    const row = item.aliases.map((alias) => byNorm.get(alias)).find((r) => r !== undefined);
    return row ? [{ id: item.id, board: row.board_count, all: row.all_count }] : [];
  });
  return json({ incidence });
}

if (import.meta.main) Deno.serve(withCors(handle));
