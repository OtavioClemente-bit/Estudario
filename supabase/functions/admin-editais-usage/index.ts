// Editais em uso pelas contas e a lista das contas (plano, uso do mês, editais, matérias), só para a equipe (app_admins).
//
// GET /admin-editais-usage  ->  { totals, items: [{ catalogId, title, users, primaryUsers, paidUsers, ... }] }
//   Lê a cópia na nuvem de cada conta (sync_heads -> bucket user-sync), junta os concursos de todo
//   mundo e casa cada um com o edital do catálogo: o mesmo nome curto e, entre os cargos dele, o que
//   tem mais matérias em comum. Concurso sem par (edital pelo PDF, nome mudado à mão) entra pelo nome.
//   Também a lista das contas (e-mail, plano, uso do mês, editais, matérias geradas): só a equipe vê.
//   O resumo de cada conta fica em admin_account_summary e só é refeito para quem sincronizou depois.
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

/** O resumo de uma conta tirado da cópia da nuvem (guardado em admin_account_summary). */
export interface AccountSummary {
  editais: UserEdital[];
  /** Tópicos com matéria gerada: título, matéria, concurso e quantas questões tem. */
  materials: { topic: string; subject: string; competition: string; questions: number }[];
  questions: number;
  answered: number;
  errors: number;
  lastStudyAt: number | null;
}

export function summaryOf(snapshot: unknown): AccountSummary {
  const root = snapshot as Record<string, unknown> | null;
  const list = (key: string) => (Array.isArray(root?.[key]) ? root![key] as Record<string, unknown>[] : []);
  const competitions = new Map(list("competitions").map((c) => [c.id, String(c.name ?? "")]));
  const subjects = new Map(list("subjects").map((s) => [s.id, { name: String(s.name ?? ""), competition: competitions.get(s.competitionId) ?? "" }]));
  const topics = new Map(list("topics").map((t) => [t.id, { title: String(t.title ?? ""), subject: subjects.get(t.subjectId) }]));
  const questions = list("questions").filter((q) => q.hidden !== true);
  const questionsByTopic = new Map<unknown, number>();
  for (const q of questions) questionsByTopic.set(q.topicId, (questionsByTopic.get(q.topicId) ?? 0) + 1);
  const withTheory = [...new Set(list("theories").map((t) => t.topicId))];
  const materials = withTheory.flatMap((id) => {
    const t = topics.get(id);
    return t ? [{ topic: t.title, subject: t.subject?.name ?? "", competition: t.subject?.competition ?? "", questions: questionsByTopic.get(id) ?? 0 }] : [];
  });
  const times = [
    ...list("attempts").map((a) => Number(a.answeredAt)),
    ...list("reviewHistory").map((r) => Number(r.reviewedAt)),
    ...list("sessions").map((s) => Number(s.completedAt)),
    ...list("focusSessions").map((s) => Number(s.completedAt)),
  ].filter((n) => Number.isFinite(n) && n > 0);
  return {
    editais: editaisOf(snapshot),
    materials: materials.slice(0, 300),
    questions: questions.length,
    answered: questions.filter((q) => Number(q.answerCount ?? 0) > 0).length,
    errors: list("errors").filter((e) => e.status !== "CORRIGIDO").length,
    lastStudyAt: times.length ? Math.max(...times) : null,
  };
}

/** Quantas cópias são relidas por chamada (as demais ficam para a próxima: o painel diz quantas faltam). */
const MAX_REFRESH = 40;

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
  const rest = async <T>(path: string, init: RequestInit = {}): Promise<T> => {
    const response = await fetch(`${url}/rest/v1/${path}`, { ...init, headers: { ...headers, "content-type": "application/json", ...(init.headers ?? {}) } });
    if (!response.ok) throw new Error(`rest ${response.status}`);
    const text = await response.text();
    return (text ? JSON.parse(text) : null) as T;
  };

  const admins = await rest<unknown[]>(`app_admins?select=user_id&user_id=eq.${encodeURIComponent(userId)}`).catch(() => []);
  if (admins.length === 0) return json({ error: { code: "FORBIDDEN" } }, 403);

  try {
    const heads = await rest<{ user_id: string; object_path: string; updated_at: string }[]>(
      `sync_heads?select=user_id,object_path,updated_at&object_path=not.is.null&order=updated_at.desc&limit=${MAX_ACCOUNTS}`,
    );
    // O resumo de cada conta fica guardado: só relê a cópia de quem sincronizou desde a última vez
    // (antes baixava a cópia de todo mundo a cada abertura e a função estourava o tempo).
    const saved = await rest<{ user_id: string; sync_updated_at: string; summary: AccountSummary }[]>(
      "admin_account_summary?select=user_id,sync_updated_at,summary",
    ).catch(() => []);
    const summaries = new Map(saved.map((row) => [row.user_id, row]));
    const stale = heads.filter((head) => {
      const s = summaries.get(head.user_id);
      return !s || Date.parse(s.sync_updated_at) < Date.parse(head.updated_at);
    });
    const refreshNow = stale.slice(0, MAX_REFRESH);
    let unreadable = 0;
    const fresh: { user_id: string; sync_updated_at: string; summary: AccountSummary; computed_at: string }[] = [];
    for (let i = 0; i < refreshNow.length; i += PARALLEL) {
      await Promise.all(refreshNow.slice(i, i + PARALLEL).map(async (head) => {
        const path = head.object_path.split("/").map(encodeURIComponent).join("/");
        const response = await fetch(`${url}/storage/v1/object/authenticated/user-sync/${path}`, { headers }).catch(() => null);
        const snapshot = response?.ok ? await response.json().catch(() => null) : null;
        if (!snapshot) { unreadable++; return; }
        const row = { user_id: head.user_id, sync_updated_at: head.updated_at, summary: summaryOf(snapshot), computed_at: new Date().toISOString() };
        fresh.push(row);
        summaries.set(head.user_id, row);
      }));
    }
    if (fresh.length) {
      await rest("admin_account_summary?on_conflict=user_id", { method: "POST", headers: { Prefer: "resolution=merge-duplicates,return=minimal" }, body: JSON.stringify(fresh) }).catch(() => null);
    }

    const profiles = await rest<{ user_id: string; plan_tier: string | null; plan_renews_at: string | null; created_at: string }[]>(
      "profiles?select=user_id,plan_tier,plan_renews_at,created_at",
    ).catch(() => []);
    const now = Date.now();
    const profileOf = new Map(profiles.map((p) => [p.user_id, p]));
    const tierOf = (id: string) => {
      const p = profileOf.get(id);
      const active = p && p.plan_tier && p.plan_tier !== "FREE" && (!p.plan_renews_at || Date.parse(p.plan_renews_at) > now);
      return active ? p!.plan_tier! : "FREE";
    };

    const accounts: Account[] = heads.flatMap((head) => {
      const s = summaries.get(head.user_id);
      return s ? [{ editais: s.summary.editais, paid: tierOf(head.user_id) !== "FREE", updatedAt: head.updated_at }] : [];
    });

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

    // ---- a lista de contas: e-mail, plano, uso do mês, editais e matérias geradas
    const emails = new Map<string, { email: string | null; lastSignIn: string | null; createdAt: string | null }>();
    for (let page = 1; page <= 10; page++) {
      const response = await fetch(`${url}/auth/v1/admin/users?page=${page}&per_page=1000`, { headers }).catch(() => null);
      if (!response?.ok) break;
      const body = await response.json() as { users?: { id: string; email?: string | null; last_sign_in_at?: string | null; created_at?: string | null }[] };
      const users = body.users ?? [];
      for (const u of users) emails.set(u.id, { email: u.email ?? null, lastSignIn: u.last_sign_in_at ?? null, createdAt: u.created_at ?? null });
      if (users.length < 1000) break;
    }
    const limits = await rest<{ plan_tier: string; feature: string; period_kind: string; quota_limit: number }[]>(
      "ai_plan_limits?select=plan_tier,feature,period_kind,quota_limit",
    ).catch(() => []);
    const ids = [...new Set([...heads.map((h) => h.user_id), ...profiles.map((p) => p.user_id)])];
    const usage: { user_id: string; feature: string; period_start: string; successful_count: number; reserved_count: number }[] = [];
    for (let i = 0; i < ids.length; i += 100) {
      usage.push(...await rest<typeof usage>(
        `ai_quota_usage?select=user_id,feature,period_start,successful_count,reserved_count&user_id=in.(${ids.slice(i, i + 100).join(",")})`,
      ).catch(() => []));
    }
    const since = new Date(now - 30 * 86_400_000).toISOString();
    const jobs: { user_id: string; feature: string; status: string; created_at: string }[] = [];
    for (let i = 0; i < ids.length; i += 100) {
      jobs.push(...await rest<typeof jobs>(
        `ai_jobs?select=user_id,feature,status,created_at&created_at=gte.${since}&user_id=in.(${ids.slice(i, i + 100).join(",")})`,
      ).catch(() => []));
    }
    const headOf = new Map(heads.map((h) => [h.user_id, h]));
    const accountList = ids.map((id) => {
      const tier = tierOf(id);
      const mine = usage.filter((u) => u.user_id === id);
      // O período atual de cada recurso: o mais recente que a conta tem.
      const latest = new Map<string, typeof mine[number]>();
      for (const u of mine) if (!latest.has(u.feature) || u.period_start > latest.get(u.feature)!.period_start) latest.set(u.feature, u);
      const quotas = limits.filter((l) => l.plan_tier === tier && l.feature !== "AD_REWARD").map((l) => ({
        feature: l.feature, period: l.period_kind, limit: l.quota_limit,
        used: (latest.get(l.feature)?.successful_count ?? 0) + (latest.get(l.feature)?.reserved_count ?? 0),
      }));
      const myJobs = jobs.filter((j) => j.user_id === id);
      const summary = summaries.get(id)?.summary ?? null;
      const p = profileOf.get(id);
      return {
        email: emails.get(id)?.email ?? null,
        createdAt: emails.get(id)?.createdAt ?? p?.created_at ?? null,
        lastSignIn: emails.get(id)?.lastSignIn ?? null,
        lastSync: headOf.get(id)?.updated_at ?? null,
        plan: tier,
        planRenewsAt: tier !== "FREE" ? p?.plan_renews_at ?? null : null,
        quotas,
        jobs30d: { total: myJobs.length, content: myJobs.filter((j) => j.feature === "CONTENT_GENERATION").length, failed: myJobs.filter((j) => /fail/i.test(j.status)).length },
        summary,
      };
    }).sort((a, b) => (b.lastSync ?? b.lastSignIn ?? "").localeCompare(a.lastSync ?? a.lastSignIn ?? ""));

    return json({
      ...result,
      totals: { ...result.totals, unreadable, pending: Math.max(0, stale.length - refreshNow.length) },
      accounts: accountList,
      generatedAt: new Date().toISOString(),
    });
  } catch {
    return json({ error: { code: "UNAVAILABLE" } }, 503);
  }
}

if (import.meta.main) Deno.serve(withCors(handle));
