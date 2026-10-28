// Confere que as ligações tópico do edital → matéria continuam entregando a mesma coisa.
//   ... conferir-ligacoes.ts --foto <arquivo>      guarda o resultado de N apelidos (antes)
//   ... conferir-ligacoes.ts --comparar <arquivo>  refaz as mesmas buscas e compara (depois)
// Usa a busca real do servidor (library_lookup) e, se ela já devolver só o caminho, lê o arquivo do
// bucket "library", do jeito que o worker faz. Compara o conteúdo inteiro da matéria e do recorte.

import { contentHash } from "./arquivos.ts";

const url = Deno.env.get("SUPABASE_URL")?.replace(/\/$/, "");
const key = Deno.env.get("SUPABASE_SERVICE_ROLE_KEY");
const mode = Deno.args[0];
const path = Deno.args[1];
if (!url || !key || !path || (mode !== "--foto" && mode !== "--comparar")) {
  console.error("Uso: --foto <arquivo> | --comparar <arquivo>");
  Deno.exit(1);
}
const headers = { apikey: key, authorization: `Bearer ${key}`, "content-type": "application/json" };

type Probe = { alias: string; board: string; topic: string | null; material: string | null; note: string | null };

async function file(p: string | null | undefined): Promise<unknown> {
  if (!p) return null;
  const r = await fetch(`${url}/storage/v1/object/library/${p.split("/").map(encodeURIComponent).join("/")}`, { headers });
  if (!r.ok) throw new Error(`arquivo ${p}: ${r.status}`);
  return await r.json();
}
async function probe(alias: string, board: string): Promise<Probe> {
  const r = await fetch(`${url}/rest/v1/rpc/library_lookup`, { method: "POST", headers, body: JSON.stringify({ p_aliases: [alias], p_board: board, p_role: "" }) });
  if (!r.ok) throw new Error(`lookup ${alias}: ${r.status} ${await r.text()}`);
  const row = (await r.json() as Array<Record<string, unknown>>)[0];
  if (!row) return { alias, board, topic: null, material: null, note: null };
  const material = row.material ?? await file(row.material_path as string | undefined);
  const note = row.note ?? await file(row.note_path as string | undefined);
  return { alias, board, topic: String(row.topic_id), material: material ? contentHash(material) : null, note: note ? contentHash(note) : null };
}

if (mode === "--foto") {
  // Apelidos espalhados pela tabela inteira + as bancas que têm recorte do mesmo tópico.
  const total = Number((await fetch(`${url}/rest/v1/library_topic_aliases?select=alias_norm`, { method: "HEAD", headers: { ...headers, prefer: "count=exact" } })).headers.get("content-range")?.split("/")[1] ?? 0);
  const probes: Probe[] = [];
  for (let i = 0; i < 300; i++) {
    const offset = Math.floor((total / 300) * i);
    const a = (await (await fetch(`${url}/rest/v1/library_topic_aliases?select=alias_norm,topic_id&order=alias_norm&offset=${offset}&limit=1`, { headers })).json() as Array<{ alias_norm: string; topic_id: string }>)[0];
    if (!a) continue;
    const n = (await (await fetch(`${url}/rest/v1/library_board_notes?select=board_norm&topic_id=eq.${encodeURIComponent(a.topic_id)}&role_norm=eq.&limit=1`, { headers })).json() as Array<{ board_norm: string }>)[0];
    probes.push(await probe(a.alias_norm, n?.board_norm ?? ""));
  }
  await Deno.writeTextFile(path, JSON.stringify(probes, null, 1));
  console.log(`Foto: ${probes.length} buscas, ${probes.filter((p) => p.topic).length} com matéria e ${probes.filter((p) => p.note).length} com recorte.`);
} else {
  const before = JSON.parse(await Deno.readTextFile(path)) as Probe[];
  let ok = 0;
  const bad: string[] = [];
  for (const b of before) {
    const now = await probe(b.alias, b.board);
    if (now.topic === b.topic && now.material === b.material && now.note === b.note) ok++;
    else bad.push(`${b.alias} [${b.board}]: antes ${b.topic}/${b.material}/${b.note} agora ${now.topic}/${now.material}/${now.note}`);
  }
  console.log(`Iguais: ${ok}/${before.length}`);
  if (bad.length) { console.log("DIFERENTES:"); bad.slice(0, 30).forEach((l) => console.log("  " + l)); Deno.exit(2); }
}
