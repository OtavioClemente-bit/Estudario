// Conta as questões oficiais (provas de verdade) de cada tópico da biblioteca, por banca e no total,
// e grava em library_topic_incidence. O plano usa para dar mais treino ao que mais cai.
//
// Uso: bash scripts/biblioteca/com-chave.sh scripts/biblioteca/incidencia.ts [--seco]
//   --seco  só mostra a conta, sem gravar.
// Lê conteudo/recortes-oficiais/<banca>/<tópico>.json (só leitura). Só grava tópicos que existem na
// biblioteca publicada; refaz a tabela inteira a cada rodada (o que sumiu das provas zera).
import { normalizeBoard } from "../../supabase/functions/_shared/library.ts";

const ROOT = "conteudo/recortes-oficiais";
const dry = Deno.args.includes("--seco");

const counts = new Map<string, number>(); // "topic|board" -> n
for await (const dir of Deno.readDir(ROOT)) {
  if (!dir.isDirectory) continue;
  for await (const file of Deno.readDir(`${ROOT}/${dir.name}`)) {
    if (!file.name.endsWith(".json")) continue;
    const data = JSON.parse(await Deno.readTextFile(`${ROOT}/${dir.name}/${file.name}`)) as { topic?: string; board?: string; questions?: unknown[] };
    const topic = data.topic ?? file.name.replace(/\.json$/, "");
    const n = Array.isArray(data.questions) ? data.questions.length : 0;
    if (n === 0) continue;
    const board = normalizeBoard(data.board ?? dir.name);
    for (const key of [`${topic}|${board}`, `${topic}|`]) counts.set(key, (counts.get(key) ?? 0) + n);
  }
}
const topics = [...new Set([...counts.keys()].map((k) => k.split("|")[0]))];
console.log(`${topics.length} tópicos com questões oficiais; ${[...counts.entries()].filter(([k]) => k.endsWith("|")).reduce((s, [, v]) => s + v, 0)} questões.`);
if (dry) Deno.exit(0);

const url = Deno.env.get("SUPABASE_URL")!.replace(/\/$/, "");
const key = Deno.env.get("SUPABASE_SERVICE_ROLE_KEY")!;
const headers = { apikey: key, authorization: `Bearer ${key}`, "content-type": "application/json" };

// Só os que estão na biblioteca publicada (a tabela tem chave estrangeira).
const known = new Set<string>();
for (let i = 0; i < topics.length; i += 150) {
  const ids = topics.slice(i, i + 150).map((t) => `"${t}"`).join(",");
  const r = await fetch(`${url}/rest/v1/library_topics?select=id&status=eq.PUBLISHED&id=in.(${encodeURIComponent(ids)})`, { headers });
  if (!r.ok) throw new Error(`library_topics: ${r.status} ${await r.text()}`);
  for (const row of await r.json() as { id: string }[]) known.add(row.id);
}
const rows = [...counts.entries()].map(([k, n]) => { const [topic_id, board_norm] = k.split("|"); return { topic_id, board_norm, real_count: n, updated_at: new Date().toISOString() }; })
  .filter((row) => known.has(row.topic_id));
console.log(`${known.size} deles estão na biblioteca publicada; ${rows.length} linhas.`);

// Grava primeiro e só depois apaga o que não veio nesta rodada: uma falha no meio nunca deixa a tabela vazia.
if (rows.length === 0) throw new Error("Nenhuma linha para gravar: nada foi alterado.");
const runStart = new Date(Date.now() - 1000).toISOString();
for (const row of rows) row.updated_at = new Date().toISOString();
for (let i = 0; i < rows.length; i += 500) {
  const r = await fetch(`${url}/rest/v1/library_topic_incidence`, { method: "POST", headers: { ...headers, prefer: "resolution=merge-duplicates" }, body: JSON.stringify(rows.slice(i, i + 500)) });
  if (!r.ok) throw new Error(`gravar: ${r.status} ${await r.text()}`);
}
const del = await fetch(`${url}/rest/v1/library_topic_incidence?updated_at=lt.${encodeURIComponent(runStart)}`, { method: "DELETE", headers });
if (!del.ok) throw new Error(`limpar o antigo: ${del.status} ${await del.text()}`);
console.log("Incidência gravada.");
