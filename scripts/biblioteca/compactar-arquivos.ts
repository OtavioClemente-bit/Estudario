// Converte para gzip os arquivos do bucket "library" que o banco ainda aponta sem ".gz" (14/11/2026):
// baixa, compacta, sobe em <caminho>.gz e troca o caminho na linha do banco. O antigo fica para o
// limpar-arquivos.ts apagar depois de 6 horas. Precisa das funções que já leem ".gz" publicadas.
//   deno run -A scripts/biblioteca/compactar-arquivos.ts
const url = "https://gaqqilzhmvkxfpvivwwv.supabase.co";
const a = JSON.parse(new TextDecoder().decode(new Deno.Command("cmd", { args: ["/c", "npx", "--no-install", "supabase", "projects", "api-keys", "--project-ref", "gaqqilzhmvkxfpvivwwv", "-o", "json"], stdout: "piped", stderr: "null" }).outputSync().stdout));
const key = (Array.isArray(a) ? a : a.keys || []).find((k: { name?: string; type?: string }) => k.name === "service_role" || k.type === "secret")?.api_key;
const h = { apikey: key, authorization: `Bearer ${key}` };
const enc = (p: string) => p.split("/").map(encodeURIComponent).join("/");
let feitos = 0, antes = 0, depois = 0, erros = 0;
async function um(tab: string, col: string, filtro: string, path: string) {
  const r = await fetch(`${url}/storage/v1/object/library/${enc(path)}`, { headers: h });
  if (!r.ok) { erros++; await r.body?.cancel(); return; }
  const raw = new Uint8Array(await r.arrayBuffer());
  const gz = new Uint8Array(await new Response(new Blob([raw]).stream().pipeThrough(new CompressionStream("gzip"))).arrayBuffer());
  const novo = path + ".gz";
  const up = await fetch(`${url}/storage/v1/object/library/${enc(novo)}`, { method: "POST", headers: { ...h, "content-type": "application/json", "x-upsert": "true", "cache-control": "max-age=31536000" }, body: gz });
  if (!up.ok) { erros++; console.log("upload", novo, up.status, (await up.text()).slice(0, 120)); return; }
  await up.body?.cancel();
  const pa = await fetch(`${url}/rest/v1/${tab}?${filtro}`, { method: "PATCH", headers: { ...h, "content-type": "application/json", prefer: "return=minimal" }, body: JSON.stringify({ [col]: novo }) });
  if (!pa.ok) { erros++; console.log("patch", pa.status, (await pa.text()).slice(0, 120)); return; }
  await pa.body?.cancel();
  feitos++; antes += raw.length; depois += gz.length;
  if (feitos % 500 === 0) console.log(feitos, (antes / 1e6).toFixed(0), "MB ->", (depois / 1e6).toFixed(0), "MB");
}
for (const [tab, col, keyCols] of [["library_topics", "material_path", ["id"]], ["library_board_notes", "note_path", ["topic_id", "board_norm", "role_norm"]]] as const) {
  const rows: Record<string, string>[] = [];
  for (let off = 0; ; off += 1000) {
    const r = await (await fetch(`${url}/rest/v1/${tab}?select=${[col, ...keyCols].join(",")}&${col}=like.*.json&offset=${off}&limit=1000`, { headers: h })).json();
    rows.push(...r); if (r.length < 1000) break;
  }
  console.log(tab, rows.length, "a compactar");
  for (let i = 0; i < rows.length; i += 8) {
    await Promise.all(rows.slice(i, i + 8).map((row) => um(tab, col, keyCols.map((k) => `${k}=eq.${encodeURIComponent(row[k])}`).join("&"), row[col])));
  }
}
console.log({ feitos, erros, antesMB: Math.round(antes / 1e6), depoisMB: Math.round(depois / 1e6) });
