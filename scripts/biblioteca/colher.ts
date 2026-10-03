// Colheita: questões de materiais gerados pela IA no app (ai_jobs) para tópicos que já têm matéria
// na biblioteca. Separa as que ainda não estão no banco da matéria, tira menções a banca/concurso e
// grava conteudo/colheita/<id>.json para revisão. Não mexe nas matérias nem no banco (só lê).
// bash scripts/biblioteca/com-chave.sh scripts/biblioteca/colher.ts
import { normalizeAlias } from "../../supabase/functions/_shared/library.ts";

const url = Deno.env.get("SUPABASE_URL")!;
const key = Deno.env.get("SUPABASE_SERVICE_ROLE_KEY")!;
type Json = Record<string, any>;
const get = async (path: string) => {
  const r = await fetch(`${url}/rest/v1/${path}`, { headers: { apikey: key, authorization: `Bearer ${key}` } });
  if (!r.ok) throw new Error(`${path}: ${r.status} ${await r.text()}`);
  return await r.json();
};

const stateFile = "conteudo/colheita/_colhidos.json";
Deno.mkdirSync("conteudo/colheita", { recursive: true });
let harvested: string[] = [];
try { harvested = JSON.parse(Deno.readTextFileSync(stateFile)); } catch { /* primeira vez */ }
const done = new Set(harvested);

// Matérias locais (para não repetir questões que já estão no banco delas).
const local = new Map<string, Json>();
for (const area of Deno.readDirSync("conteudo/materias")) {
  if (!area.isDirectory) continue;
  for (const file of Deno.readDirSync(`conteudo/materias/${area.name}`)) {
    if (!file.name.endsWith(".json") || file.name.startsWith("_")) continue;
    const m = JSON.parse(Deno.readTextFileSync(`conteudo/materias/${area.name}/${file.name}`));
    local.set(m.id, m);
  }
}

const aliasRows = await get("library_topic_aliases?select=alias_norm,topic_id") as { alias_norm: string; topic_id: string }[];
const routeRows = await get("library_topic_routes?select=topic_norm,topic_ids&coverage=eq.FULL") as { topic_norm: string; topic_ids: string[] }[];
const aliasOf = new Map(aliasRows.map((a) => [a.alias_norm, a.topic_id]));
for (const r of routeRows) if (r.topic_ids.length === 1) aliasOf.set(r.topic_norm, r.topic_ids[0]);

const jobs = await get(
  "ai_jobs?select=id,created_at,topic:request_payload->input->topicPath,proposal&feature=eq.CONTENT_GENERATION&status=eq.SUCCEEDED&proposal->>modelVersion=neq.biblioteca&order=created_at.desc&limit=200",
) as { id: string; created_at: string; topic: string[] | null; proposal: Json | null }[];

// Frase que cita banca, concurso, caderno de prova ou link: sai da explicação.
const BOARD_TALK = /\b(banca|caderno|padr[aã]o de cobran[cç]a|fumarc|cebraspe|cespe|fgv|fcc|vunesp|idecan|ibfc|quadrix|aocp|consulplan|instituto|trt|trf|tj[a-z]{0,2}|pmmg|cbmmg|edital)\b|https?:\/\//i;
const clean = (text: string) => text.split(/(?<=[.!?])\s+/).filter((s) => !BOARD_TALK.test(s)).join(" ").trim();

const out = new Map<string, Json[]>();
let used = 0;
for (const job of jobs) {
  if (done.has(job.id) || !job.topic?.length || !job.proposal) continue;
  const path = job.topic.map(normalizeAlias).filter(Boolean);
  const topicId = aliasOf.get(path.join(" ")) ?? aliasOf.get(path.at(-1) ?? "");
  const material = topicId ? local.get(topicId) : undefined;
  done.add(job.id);
  if (!topicId || !material) continue;
  const known = new Set((material.questions as Json[]).map((q) => String(q.statement).trim().toLowerCase()));
  const fresh = ((job.proposal.questions ?? []) as Json[])
    .filter((q) => !known.has(String(q.statement).trim().toLowerCase()))
    .map((q) => ({ ...q, explanation: clean(String(q.explanation ?? "")), _job: job.id, _section: q.section }));
  if (fresh.length === 0) continue;
  used++;
  out.set(topicId, [...(out.get(topicId) ?? []), ...fresh]);
}

for (const [topicId, questions] of out) {
  const file = `conteudo/colheita/${topicId}.json`;
  let previous: Json[] = [];
  try { previous = JSON.parse(Deno.readTextFileSync(file)).questions; } catch { /* novo */ }
  Deno.writeTextFileSync(file, JSON.stringify({ topic: topicId, questions: [...previous, ...questions] }, null, 2) + "\n");
  console.log(`${topicId}: +${questions.length} questões → ${file}`);
}
Deno.writeTextFileSync(stateFile, JSON.stringify([...done], null, 2) + "\n");
console.log(out.size === 0 ? "Nada novo para colher." : `\n${used} material(is) aproveitado(s). Revise os arquivos de conteudo/colheita/ antes de juntar às matérias.`);
