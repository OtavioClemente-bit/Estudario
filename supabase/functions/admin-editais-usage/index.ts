// Editais em uso pelas contas, só para a equipe (app_admins).
//
// GET /admin-editais-usage  ->  { totals, items: [{ catalogId, title, users, primaryUsers, paidUsers, ... }] }
//   Lê a cópia na nuvem de cada conta (sync_heads -> bucket user-sync), junta os concursos de todo
//   mundo e casa cada um com o edital do catálogo: o mesmo nome curto e, entre os cargos dele, o que
//   tem mais matérias em comum. Concurso sem par (edital pelo PDF, nome mudado à mão) entra pelo nome.
//   Só leitura; nada de dado pessoal sai daqui, só contagens.
import { withCors } from "../_shared/cors.ts";
import { authenticateSupabaseRequest, AuthError } from "../_shared/auth.ts";

const MAX_ACCOUNTS = 5000;
const PARALLEL = 10;

function json(body: unknown, status = 200): Response {
  return new Response(JSON.stringify(body), { status, headers: { "content-type": "application/json; charset=utf-8", "cache-control": "no-store" } });
}

export function norm(value: string): string {
  return value.normalize("NFD").replace(/[̀-ͯ]/g, "").toLowerCase().replace(/[^a-z0-9]+/g, " ").trim();
}

/** Um concurso de uma conta, como está na cópia da nuvem. */
export interface UserEdital { name: string; primary: boolean; subjects: string[]; topics: number; studied: number }

export interface CatalogRow {
  id: string; short_name: string; role: string; agency: string; board: string | null; year: number | null;
  topic_count: number; ready_topic_count: number; subjects: { name?: string }[];
}

/** Os concursos de uma cópia (JSON do app ou do site). */
export function editaisOf(snapshot: unknown): UserEdital[] {
  const root = snapshot as Record<string, unknown> | null;
  const list = (key: string) => (Array.isArray(root?.[key]) ? root![key] as Record<string, unknown>[] : []);
  const subjects = list("subjects");
  const topics = list("topics");
  const topicsBySubject = new Map<unknown, { total: number; studied: number }>();
  for (const t of topics) {
    const entry = topicsBySubject.get(t.subjectId) ?? { total: 0, studied: 0 };
    entry.total++;
    if (typeof t.status === "string" && t.status !== "NAO_ESTUDADO") entry.studied++;
    topicsBySubject.set(t.subjectId, entry);
  }
  return list("competitions").flatMap((c) => {
    if (typeof c.name !== "string" || !c.name.trim()) return [];
    const own = subjects.filter((s) => s.competitionId === c.id);
    const counts = own.map((s) => topicsBySubject.get(s.id) ?? { total: 0, studied: 0 });
    return [{
      name: c.name.trim(),
      primary: c.primary === true || c.isPrimary === true,
      subjects: own.map((s) => typeof s.name === "string" ? s.name : "").filter(Boolean),
      topics: counts.reduce((sum, x) => sum + x.total, 0),
      studied: counts.reduce((sum, x) => sum + x.studied, 0),
    }];
  });
}

/** O edital do catálogo que é este concurso: mesmo nome curto (e cargo, se veio no nome) e mais matérias em comum. */
export function matchCatalog(edital: UserEdital, catalog: CatalogRow[]): CatalogRow | null {
  const [short, role] = edital.name.split(" · ").map(norm);
  const candidates = catalog.filter((row) => norm(row.short_name) === short && (!role || norm(row.role) === role));
  if (candidates.length === 0) return null;
  const mine = new Set(edital.subjects.map(norm));
  let best: CatalogRow | null = null, bestScore = -1;
  for (const row of candidates) {
    const theirs = row.subjects.map((s) => norm(s.name ?? ""));
    const score = theirs.filter((s) => mine.has(s)).length / Math.max(theirs.length, mine.size, 1);
    if (score > bestScore) { best = row; bestScore = score; }
  }
  // Só um cargo com esse nome: é ele. Vários: precisa de matérias em comum.
  return candidates.length === 1 || bestScore > 0 ? best : null;
}

export interface UsageItem {
  key: string; catalogId: string | null; title: string; details: string;
  users: number; primaryUsers: number; paidUsers: number;
  topicCount: number; readyPercent: number | null; studiedPercent: number; lastActive: string | null;
}

interface Account { editais: UserEdital[]; paid: boolean; updatedAt: string | null }

export function aggregate(accounts: Account[], catalog: CatalogRow[]): { totals: Record<string, number>; items: UsageItem[] } {
  const items = new Map<string, UsageItem & { topicsSum: number; studiedSum: number }>();
  for (const account of accounts) {
    const seen = new Set<string>();
    for (const edital of account.editais) {
      const row = matchCatalog(edital, catalog);
      const key = row ? `catalogo:${row.id}` : `nome:${norm(edital.name)}`;
      if (seen.has(key)) continue;
      seen.add(key);
      const item = items.get(key) ?? {
        key, catalogId: row?.id ?? null,
        title: row ? `${row.short_name} · ${row.role}` : edital.name,
        details: row ? [row.agency !== row.short_name ? row.agency : null, row.board, row.year].filter(Boolean).join(" · ") : "fora do catálogo (PDF ou nome próprio)",
        users: 0, primaryUsers: 0, paidUsers: 0,
        topicCount: row?.topic_count ?? edital.topics,
        readyPercent: row && row.topic_count > 0 ? Math.round(row.ready_topic_count * 100 / row.topic_count) : null,
        studiedPercent: 0, lastActive: null, topicsSum: 0, studiedSum: 0,
      };
      item.users++;
      if (edital.primary) item.primaryUsers++;
      if (account.paid) item.paidUsers++;
      item.topicsSum += edital.topics;
      item.studiedSum += edital.studied;
      if (account.updatedAt && (!item.lastActive || account.updatedAt > item.lastActive)) item.lastActive = account.updatedAt;
      items.set(key, item);
    }
  }
  const list = [...items.values()].map(({ topicsSum, studiedSum, ...item }) => ({
    ...item,
    studiedPercent: topicsSum > 0 ? Math.round(studiedSum * 100 / topicsSum) : 0,
  })).sort((a, b) => b.users - a.users || a.title.localeCompare(b.title));
  return {
    totals: {
      accounts: accounts.length,
      withEditais: accounts.filter((a) => a.editais.length > 0).length,
      editais: list.length,
      fromCatalog: list.filter((i) => i.catalogId).length,
    },
    items: list,
  };
}

async function handle(request: Request): Promise<Response> {
  if (request.method !== "GET") return json({ error: { code: "METHOD_NOT_ALLOWED" } }, 405);
  let userId: string;
  try {
    userId = (await authenticateSupabaseRequest(request)).userId;
  } catch (error) {
    return json({ error: { code: error instanceof AuthError ? error.code : "AUTH_UNAVAILABLE" } }, error instanceof AuthError ? error.status : 503);
  }
  const url = Deno.env.get("SUPABASE_URL")?.trim()?.replace(/\/$/, ""), key = Deno.env.get("SUPABASE_SERVICE_ROLE_KEY")?.trim();
  if (!url || !key) return json({ error: { code: "UNAVAILABLE" } }, 503);
  const headers = { apikey: key, authorization: `Bearer ${key}`, accept: "application/json" };
  const rest = async <T>(path: string): Promise<T> => {
    const response = await fetch(`${url}/rest/v1/${path}`, { headers });
    if (!response.ok) throw new Error(`rest ${response.status}`);
    return await response.json() as T;
  };

  const admins = await rest<unknown[]>(`app_admins?select=user_id&user_id=eq.${encodeURIComponent(userId)}`).catch(() => []);
  if (admins.length === 0) return json({ error: { code: "FORBIDDEN" } }, 403);

  try {
    const heads = await rest<{ user_id: string; object_path: string; updated_at: string }[]>(
      `sync_heads?select=user_id,object_path,updated_at&object_path=not.is.null&order=updated_at.desc&limit=${MAX_ACCOUNTS}`,
    );
    const profiles = await rest<{ user_id: string; plan_tier: string; plan_renews_at: string | null }[]>(
      "profiles?select=user_id,plan_tier,plan_renews_at&plan_tier=neq.FREE",
    ).catch(() => []);
    const now = Date.now();
    const paid = new Set(profiles.filter((p) => !p.plan_renews_at || Date.parse(p.plan_renews_at) > now).map((p) => p.user_id));

    const accounts: Account[] = [];
    let unreadable = 0;
    for (let i = 0; i < heads.length; i += PARALLEL) {
      await Promise.all(heads.slice(i, i + PARALLEL).map(async (head) => {
        const path = head.object_path.split("/").map(encodeURIComponent).join("/");
        const response = await fetch(`${url}/storage/v1/object/authenticated/user-sync/${path}`, { headers }).catch(() => null);
        const snapshot = response?.ok ? await response.json().catch(() => null) : null;
        if (!snapshot) { unreadable++; return; }
        accounts.push({ editais: editaisOf(snapshot), paid: paid.has(head.user_id), updatedAt: head.updated_at });
      }));
    }

    // Só os cargos do catálogo com os nomes curtos que apareceram (o catálogo inteiro é grande).
    const shorts = [...new Set(accounts.flatMap((a) => a.editais.map((e) => e.name.split(" · ")[0].trim())))];
    const catalog: CatalogRow[] = [];
    for (let i = 0; i < shorts.length; i += 40) {
      const list = shorts.slice(i, i + 40).map((s) => `"${s.replace(/"/g, '\\"')}"`).join(",");
      catalog.push(...await rest<CatalogRow[]>(
        `exam_catalog?select=id,short_name,role,agency,board,year,topic_count,ready_topic_count,subjects&status=eq.PUBLISHED&short_name=in.(${encodeURIComponent(list)})`,
      ).catch(() => []));
    }
    const result = aggregate(accounts, catalog);
    return json({ ...result, totals: { ...result.totals, unreadable }, generatedAt: new Date().toISOString() });
  } catch {
    return json({ error: { code: "UNAVAILABLE" } }, 503);
  }
}

if (import.meta.main) Deno.serve(withCors(handle));
