// Atualização das matérias que vieram da biblioteca, sem apagar nada na conta da pessoa.
//
// POST /library-refresh  { items: [Item, ...] }  ->  { items: [Result, ...] }
//
// O app e o site perguntam uma vez por dia, com os tópicos que têm matéria e a marca ("stamp") da
// versão que já têm. A conferência é barata: só os caminhos dos arquivos da biblioteca (que levam o
// hash do conteúdo), sem baixar nada. Quando a matéria ou o recorte da banca mudou, a função monta
// de novo, do mesmo jeito que o gerador monta (mesmo foco do tópico, mesma banca, mesmas partes), e
// devolve também as questões que a pessoa já tem, corrigidas, reconhecidas pelo enunciado. Quem
// aplica, no lugar e sem perder respostas, grifos e caderno de erros, é o app e o site.
//
// Matéria antiga, sem marca: só é "adotada" se os capítulos dela batem com os da biblioteca (uma
// matéria que veio da IA nunca é trocada pela da biblioteca). Não usa IA; precisa de conta.
import { withCors } from "../_shared/cors.ts";
import { authenticateSupabaseRequest, AuthError } from "../_shared/auth.ts";
import { breakAssertivas } from "../_shared/content-repair.ts";
import {
  assembleFromLibrary, deliverQuestion, foreignStateMaterial, GENERAL_BOARD_NORM, inferFocus, type LibraryFocus,
  lookupAliases, normalizeAlias, normalizeBoard,
} from "../_shared/library.ts";
import { CONTENT_BLOCKS, type ContentGenerationOptions } from "../_shared/text-job-input.ts";

type Json = Record<string, unknown>;

const MAX_ITEMS = 300;
/** Tópicos montados de uma vez num pedido (os demais voltam como "later" e vêm no dia seguinte). */
const MAX_BUILDS = 25;
const STYLES = new Set(["MIXED", "FIVE_OPTIONS", "FOUR_OPTIONS", "TRUE_FALSE"]);

export interface RefreshItem {
  /** O id do tópico na conta da pessoa (só para casar a resposta). */
  id: string;
  subjectName: string | null;
  topicPath: string[];
  board: string | null;
  role: string | null;
  /** A marca da versão que a pessoa tem; nula numa matéria antiga (pede adoção). */
  stamp: string | null;
  blocks: string[];
  questionStyle: ContentGenerationOptions["questionStyle"];
  /** Títulos dos capítulos que a pessoa tem (para a adoção de matéria antiga). */
  chapterTitles: string[];
  /** Enunciados das questões que a pessoa tem (até 300 caracteres cada), na ordem dela. */
  questions: string[];
}

export interface LookupRow {
  topic_id: string;
  version: number;
  material_path: string | null;
  note_path: string | null;
  material: Json | null;
  note: Json | null;
  focus: LibraryFocus | null;
}

export interface RefreshDeps {
  lookup(aliases: string[], boardNorm: string, roleNorm: string): Promise<LookupRow | null>;
  generalNote(topicId: string): Promise<{ note: Json | null; path: string | null }>;
  file(path: string | null): Promise<Json | null>;
}

export type RefreshResult =
  | { id: string; status: "none" | "same" | "later" | "skip"; stamp?: string }
  | { id: string; status: "update"; stamp: string; libraryTopic: string; content: Json; questions: Array<{ index: number; question: Json }> };

const text = (value: unknown, max: number) => typeof value === "string" ? value.slice(0, max) : null;

/** Os itens do pedido, limpos (o mesmo cuidado com tamanho dos outros endpoints). */
export function itemsOf(body: unknown): RefreshItem[] {
  const raw = (body as { items?: unknown } | null)?.items;
  if (!Array.isArray(raw)) return [];
  return raw.slice(0, MAX_ITEMS).flatMap((entry) => {
    const e = (entry ?? {}) as Json;
    if ((typeof e.id !== "string" && typeof e.id !== "number") || !Array.isArray(e.topicPath)) return [];
    const topicPath = e.topicPath.filter((part): part is string => typeof part === "string").slice(-6).map((part) => part.slice(0, 400));
    if (topicPath.length === 0) return [];
    const strings = (value: unknown, n: number, max: number) => Array.isArray(value) ? value.filter((v): v is string => typeof v === "string").slice(0, n).map((v) => v.slice(0, max)) : [];
    const blocks = strings(e.blocks, 10, 40).filter((b) => (CONTENT_BLOCKS as readonly string[]).includes(b));
    return [{
      id: String(e.id),
      subjectName: text(e.subjectName, 300),
      topicPath,
      board: text(e.board, 80),
      role: text(e.role, 120),
      stamp: text(e.stamp, 120),
      blocks: blocks.length ? blocks : [...CONTENT_BLOCKS],
      questionStyle: (STYLES.has(String(e.questionStyle)) ? e.questionStyle : "MIXED") as ContentGenerationOptions["questionStyle"],
      chapterTitles: strings(e.chapterTitles, 12, 200),
      questions: strings(e.questions, 400, 300),
    }];
  });
}

/** A marca da versão: muda quando a matéria ou o recorte usado muda (os caminhos levam o hash do conteúdo). */
export function stampOf(row: Pick<LookupRow, "topic_id" | "version" | "material_path">, notePath: string | null): string {
  let h = 2166136261;
  for (const char of `${row.topic_id}|${row.version}|${row.material_path ?? ""}|${notePath ?? ""}`) h = Math.imul(h ^ char.charCodeAt(0), 16777619);
  return `${row.version}.${(h >>> 0).toString(36)}`;
}

const squash = (value: string) => value.replace(/\s+/g, "").toLowerCase();
const plain = (value: string) => value.normalize("NFD").replace(/[̀-ͯ]/g, "").toLowerCase().replace(/[^a-z0-9]+/g, " ").trim();

/**
 * Acha, para cada questão da pessoa, a mesma questão no banco da biblioteca (matéria + recorte):
 * pelo enunciado inteiro (como o app guardou: com ou sem as assertivas quebradas, até 300 letras)
 * ou, se a correção mexeu no fim do enunciado, pelas primeiras 80 letras.
 */
export function matchQuestions(mine: string[], pool: Json[]): Array<{ index: number; question: Json }> {
  const exact = new Map<string, Json>();
  const prefix = new Map<string, Json | null>();
  for (const q of pool) {
    const raw = String(q.statement ?? "").trim();
    for (const form of [raw, breakAssertivas(raw)]) {
      exact.set(squash(form.slice(0, 300)), q);
      const head = form.length <= 300 ? form : form.slice(0, 150) + form.slice(-150);
      exact.set(squash(head), q);
    }
    const p = plain(raw).slice(0, 80);
    if (p.length >= 40) prefix.set(p, prefix.has(p) && prefix.get(p) !== q ? null : q);
  }
  const out: Array<{ index: number; question: Json }> = [];
  mine.forEach((statement, index) => {
    const hit = exact.get(squash(statement.slice(0, 300))) ?? (() => {
      const p = plain(statement).slice(0, 80);
      return p.length >= 40 ? prefix.get(p) ?? undefined : undefined;
    })();
    if (hit) out.push({ index, question: hit });
  });
  return out;
}

/** Matéria antiga, sem marca: só é da biblioteca se pelo menos 60% dos capítulos dela estão lá. */
export function looksLikeLibrary(chapterTitles: string[], material: Json): boolean {
  if (chapterTitles.length === 0) return false;
  const known = new Set(((material.chapters as Json[]) ?? []).map((c) => plain(String(c.title ?? ""))));
  const hits = chapterTitles.filter((title) => known.has(plain(title))).length;
  return hits / chapterTitles.length >= 0.6;
}

export async function refreshItem(item: RefreshItem, deps: RefreshDeps, builds: { left: number }): Promise<RefreshResult> {
  const aliases = lookupAliases({ topicPath: item.topicPath, subjectName: item.subjectName });
  const boardNorm = normalizeBoard(item.board);
  const row = aliases.length ? await deps.lookup(aliases, boardNorm, normalizeAlias(item.role)) : null;
  if (!row || foreignStateMaterial(row.topic_id, [item.subjectName, ...item.topicPath])) return { id: item.id, status: "none" };
  // Sem recorte desta banca: o "Padrão das provas" (como no gerador).
  let notePath = row.note_path;
  let note = row.note;
  if (!notePath && !note) {
    const general = await deps.generalNote(row.topic_id);
    notePath = general.path;
    note = general.note;
  }
  const stamp = stampOf(row, notePath);
  if (item.stamp === stamp) return { id: item.id, status: "same", stamp };
  if (builds.left <= 0) return { id: item.id, status: "later" };
  builds.left--;
  const material = row.material ?? await deps.file(row.material_path);
  if (!material) return { id: item.id, status: "later" };
  if (!item.stamp && !looksLikeLibrary(item.chapterTitles, material)) return { id: item.id, status: "skip" };
  note = note ?? await deps.file(notePath);
  const focus = row.focus ?? inferFocus(material, item.topicPath[item.topicPath.length - 1], row.topic_id);
  const options: ContentGenerationOptions = {
    blocks: [...new Set([...item.blocks, "QUESTIONS"])] as ContentGenerationOptions["blocks"],
    depth: "DEEP", questionCount: 1, questionStyle: item.questionStyle, difficulty: "MIXED",
  };
  // As partes de texto (teoria, resumo, cartões, dicas…) saem da mesma montagem do gerador; as
  // questões, não: a pessoa recebe as dela corrigidas, e não uma seleção nova.
  const assembled = assembleFromLibrary(material, note, { board: item.board, options, avoidStatements: [], topicPath: item.topicPath }, `refresh:${row.topic_id}`, focus, true);
  if (!assembled) return { id: item.id, status: "later" };
  const content: Json = { ...assembled };
  delete content.questions;
  for (const block of ["THEORY", "SUMMARY", "QUICK_REVIEW", "TIPS_TRAPS", "ACTIVE_RECALL"]) {
    if (item.blocks.includes(block)) continue;
    if (block === "THEORY") content.chapters = [];
    if (block === "SUMMARY") content.summary = "";
    if (block === "QUICK_REVIEW") { content.flashcards = []; content.quickReview = ""; }
    if (block === "TIPS_TRAPS") { content.tips = []; content.traps = []; }
    if (block === "ACTIVE_RECALL") content.activeRecall = [];
  }
  const pool = [...((material.questions as Json[]) ?? []), ...((note?.questions as Json[] | undefined) ?? [])];
  const questions = matchQuestions(item.questions, pool).map(({ index, question }) => ({ index, question: deliverQuestion(question, item.questionStyle, `refresh:${row.topic_id}:${index}`) }));
  return { id: item.id, status: "update", stamp, libraryTopic: row.topic_id, content, questions };
}

function json(body: unknown, status = 200): Response {
  return new Response(JSON.stringify(body), { status, headers: { "content-type": "application/json; charset=utf-8", "cache-control": "no-store" } });
}

export function serviceDeps(url: string, key: string): RefreshDeps {
  const headers = { apikey: key, authorization: `Bearer ${key}`, "content-type": "application/json", accept: "application/json" };
  const files = new Map<string, Json | null>();
  return {
    async lookup(aliases, boardNorm, roleNorm) {
      const response = await fetch(`${url}/rest/v1/rpc/library_lookup`, { method: "POST", headers, body: JSON.stringify({ p_aliases: aliases, p_board: boardNorm, p_role: roleNorm }) });
      if (!response.ok) { await response.body?.cancel().catch(() => {}); return null; }
      return ((await response.json()) as LookupRow[])[0] ?? null;
    },
    async generalNote(topicId) {
      const response = await fetch(`${url}/rest/v1/library_board_notes?select=note,note_path&topic_id=eq.${encodeURIComponent(topicId)}&board_norm=eq.${GENERAL_BOARD_NORM}&role_norm=eq.&limit=1`, { headers });
      if (!response.ok) { await response.body?.cancel().catch(() => {}); return { note: null, path: null }; }
      const row = ((await response.json()) as Array<{ note: Json | null; note_path: string | null }>)[0];
      return { note: row?.note ?? null, path: row?.note_path ?? null };
    },
    async file(path) {
      if (!path) return null;
      if (files.has(path)) return files.get(path)!;
      const response = await fetch(`${url}/storage/v1/object/library/${path.split("/").map(encodeURIComponent).join("/")}`, { headers: { apikey: key, authorization: `Bearer ${key}` } });
      const value = response.ok ? await response.json() as Json : null;
      if (!response.ok) await response.body?.cancel().catch(() => {});
      files.set(path, value);
      return value;
    },
  };
}

async function handle(request: Request): Promise<Response> {
  if (request.method !== "POST") return json({ error: { code: "METHOD_NOT_ALLOWED" } }, 405);
  try {
    await authenticateSupabaseRequest(request);
  } catch (error) {
    return json({ error: { code: error instanceof AuthError ? error.code : "AUTH_UNAVAILABLE" } }, error instanceof AuthError ? error.status : 503);
  }
  const items = itemsOf(await request.json().catch(() => null));
  if (items.length === 0) return json({ items: [] });
  const url = Deno.env.get("SUPABASE_URL")?.trim().replace(/\/$/, ""), key = Deno.env.get("SUPABASE_SERVICE_ROLE_KEY")?.trim();
  if (!url || !key) return json({ error: { code: "UNAVAILABLE" } }, 503);
  const deps = serviceDeps(url, key);
  const builds = { left: MAX_BUILDS };
  const results: RefreshResult[] = [];
  for (const item of items) results.push(await refreshItem(item, deps, builds).catch(() => ({ id: item.id, status: "later" as const })));
  return json({ items: results });
}

if (import.meta.main) Deno.serve(withCors(handle));
