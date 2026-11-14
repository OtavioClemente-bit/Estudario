// Apaga do bucket "library" as versões antigas de matérias e recortes que nenhuma linha do banco usa mais.
// Cada publicação sobe um arquivo novo (o caminho leva o hash do conteúdo) e o antigo ficava para sempre:
// em 14/11 eram 2,45 GB num plano de 1,1 GB. Só apaga o que (1) não está em library_topics.material_path nem
// em library_board_notes.note_path e (2) foi criado há mais de 6 horas (um worker pode ainda estar lendo).
//   deno run -A scripts/biblioteca/limpar-arquivos.ts            → só mostra quanto apagaria
//   deno run -A scripts/biblioteca/limpar-arquivos.ts --apagar   → apaga
const url = "https://gaqqilzhmvkxfpvivwwv.supabase.co";
const a = JSON.parse(new TextDecoder().decode(new Deno.Command("cmd", { args: ["/c", "npx", "--no-install", "supabase", "projects", "api-keys", "--project-ref", "gaqqilzhmvkxfpvivwwv", "-o", "json"], stdout: "piped", stderr: "null" }).outputSync().stdout));
const key = (Array.isArray(a) ? a : a.keys || []).find((k: { name?: string; type?: string }) => k.name === "service_role" || k.type === "secret")?.api_key;
const h = { apikey: key, authorization: `Bearer ${key}`, "content-type": "application/json" };
const usados = new Set<string>();
for (const [tab, col] of [["library_topics", "material_path"], ["library_board_notes", "note_path"]]) {
  for (let off = 0; ; off += 1000) {
    const r = await (await fetch(`${url}/rest/v1/${tab}?select=${col}&${col}=not.is.null&offset=${off}&limit=1000`, { headers: h })).json();
    for (const x of r) usados.add(String(x[col]));
    if (r.length < 1000) break;
  }
}
const limite = Date.now() - 6 * 3600e3;
const apagar: string[] = []; let tot = 0, tamApagar = 0, n = 0;
async function walk(prefix: string) {
  for (let off = 0; ; off += 1000) {
    const r = await (await fetch(`${url}/storage/v1/object/list/library`, { method: "POST", headers: h, body: JSON.stringify({ prefix, limit: 1000, offset: off }) })).json();
    if (!Array.isArray(r) || !r.length) break;
    for (const o of r) {
      const p = prefix ? `${prefix}/${o.name}` : o.name;
      if (o.id === null) { await walk(p); continue; }
      n++; const s = o.metadata?.size ?? 0; tot += s;
      if (!usados.has(p) && Date.parse(o.created_at) < limite) { apagar.push(p); tamApagar += s; }
    }
    if (r.length < 1000) break;
  }
}
await walk("");
console.log(`${n} arquivos (${(tot / 1e9).toFixed(2)} GB); em uso: ${usados.size}; a apagar: ${apagar.length} (${(tamApagar / 1e9).toFixed(2)} GB)`);
if (Deno.args.includes("--apagar")) {
  for (let i = 0; i < apagar.length; i += 500) {
    const r = await fetch(`${url}/storage/v1/object/library`, { method: "DELETE", headers: h, body: JSON.stringify({ prefixes: apagar.slice(i, i + 500) }) });
    if (!r.ok) { console.log("erro", r.status, await r.text()); break; }
    await r.body?.cancel();
  }
  console.log("apagados");
}
