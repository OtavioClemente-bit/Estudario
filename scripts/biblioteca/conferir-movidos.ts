// Confere cada matéria e recorte já movido para arquivo contra o backup tirado antes da mudança.
//   deno run --allow-env --allow-net --allow-read scripts/biblioteca/conferir-movidos.ts <pasta do backup>
import { contentHash } from "./arquivos.ts";
const url = Deno.env.get("SUPABASE_URL")!; const key = Deno.env.get("SUPABASE_SERVICE_ROLE_KEY")!;
const h = { apikey: key, authorization: `Bearer ${key}` };
const dir = Deno.args[0];
const back = new Map<string, string>();
for (const line of (await Deno.readTextFile(`${dir}/library_topics.jsonl`)).split("\n")) if (line) { const r = JSON.parse(line); back.set("t:" + r.id, contentHash(r.material)); }
for (const line of (await Deno.readTextFile(`${dir}/library_board_notes.jsonl`)).split("\n")) if (line) { const r = JSON.parse(line); back.set(`n:${r.topic_id}|${r.board_norm}|${r.role_norm}`, contentHash(r.note)); }
const file = async (p: string) => { const r = await fetch(`${url}/storage/v1/object/library/${p.split("/").map(encodeURIComponent).join("/")}`, { headers: h }); if (!r.ok) throw new Error(p + " " + r.status); return await r.json(); };
let ok = 0, bad = 0, missing = 0;
for (let from = 0; ; from += 1000) {
  const rows = await (await fetch(`${url}/rest/v1/library_topics?select=id,material_path&material=is.null&order=id`, { headers: { ...h, range: `${from}-${from + 999}` } })).json() as Array<{ id: string; material_path: string }>;
  for (const r of rows) { const want = back.get("t:" + r.id); if (!want) { missing++; continue; } contentHash(await file(r.material_path)) === want ? ok++ : (bad++, console.log("DIFERENTE matéria", r.id)); }
  if (rows.length < 1000) break;
}
for (let from = 0; ; from += 1000) {
  const rows = await (await fetch(`${url}/rest/v1/library_board_notes?select=topic_id,board_norm,role_norm,note_path&note=is.null&order=topic_id,board_norm,role_norm`, { headers: { ...h, range: `${from}-${from + 999}` } })).json() as Array<{ topic_id: string; board_norm: string; role_norm: string; note_path: string }>;
  for (const r of rows) { const want = back.get(`n:${r.topic_id}|${r.board_norm}|${r.role_norm}`); if (!want) { missing++; continue; } contentHash(await file(r.note_path)) === want ? ok++ : (bad++, console.log("DIFERENTE recorte", r.topic_id, r.board_norm)); }
  if (rows.length < 1000) break;
}
console.log(`movidos conferidos com o backup: ${ok} iguais, ${bad} diferentes, ${missing} sem par no backup`);
if (bad) Deno.exit(2);
