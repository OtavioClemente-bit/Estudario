// Uma vez só: tira do banco as matérias (library_topics.material) e os recortes de banca
// (library_board_notes.note) que ainda estão lá e os grava como arquivos no bucket "library",
// deixando no banco só o caminho. Depende da migração 20261028180000_library_in_storage.sql.
//
//   deno run --allow-env --allow-net scripts/biblioteca/mover-para-arquivos.ts            só conta
//   deno run --allow-env --allow-net scripts/biblioteca/mover-para-arquivos.ts --mover    move
//
// Pode ser interrompido e rodado de novo: só pega o que ainda tem o JSON no banco. Cada linha só é
// esvaziada depois que o arquivo dela foi gravado. No fim, o espaço volta com VACUUM FULL (ver abaixo).

import { notePath, topicPath, uploadLibraryFile } from "./arquivos.ts";

const url = Deno.env.get("SUPABASE_URL")?.replace(/\/$/, "");
const key = Deno.env.get("SUPABASE_SERVICE_ROLE_KEY");
if (!url || !key) {
  console.error("Defina SUPABASE_URL e SUPABASE_SERVICE_ROLE_KEY no ambiente.");
  Deno.exit(1);
}
const move = Deno.args.includes("--mover");
const headers = { apikey: key, authorization: `Bearer ${key}`, "content-type": "application/json" };

async function get<T>(path: string): Promise<T> {
  const response = await fetch(`${url}/rest/v1/${path}`, { headers });
  if (!response.ok) throw new Error(`GET ${path}: ${response.status} ${await response.text()}`);
  return await response.json() as T;
}
async function patch(path: string, body: unknown): Promise<void> {
  const response = await fetch(`${url}/rest/v1/${path}`, { method: "PATCH", headers: { ...headers, prefer: "return=minimal" }, body: JSON.stringify(body) });
  if (!response.ok) throw new Error(`PATCH ${path}: ${response.status} ${await response.text()}`);
  await response.body?.cancel().catch(() => {});
}
async function count(path: string): Promise<number> {
  const response = await fetch(`${url}/rest/v1/${path}`, { method: "HEAD", headers: { ...headers, prefer: "count=exact" } });
  await response.body?.cancel().catch(() => {});
  return Number(response.headers.get("content-range")?.split("/")[1] ?? 0);
}

const topicsLeft = await count("library_topics?select=id&material=not.is.null");
const notesLeft = await count("library_board_notes?select=topic_id&note=not.is.null");
console.log(`No banco ainda: ${topicsLeft} matérias e ${notesLeft} recortes.`);
if (!move) {
  console.log("Nada foi mudado (rode com --mover para mover).");
  Deno.exit(0);
}

// Matérias: em páginas pequenas (cada uma tem ~100 KB).
let topics = 0;
for (;;) {
  const page = await get<Array<{ id: string; version: number; material: Record<string, unknown> }>>(
    "library_topics?select=id,version,material&material=not.is.null&order=id&limit=10",
  );
  if (page.length === 0) break;
  for (const row of page) {
    const path = topicPath(row.id, row.version, row.material);
    await uploadLibraryFile(url, key, path, row.material);
    const covers = (row.material.scope as Record<string, unknown> | undefined)?.covers ?? null;
    await patch(`library_topics?id=eq.${encodeURIComponent(row.id)}`, { material: null, material_path: path, covers });
    topics++;
  }
  console.log(`  matérias movidas: ${topics}/${topicsLeft}`);
}

// Recortes (os escritos à mão e os automáticos).
let notes = 0;
for (;;) {
  const page = await get<Array<{ topic_id: string; board_norm: string; role_norm: string; note: Record<string, unknown> }>>(
    "library_board_notes?select=topic_id,board_norm,role_norm,note&note=not.is.null&order=topic_id,board_norm,role_norm&limit=50",
  );
  if (page.length === 0) break;
  for (const row of page) {
    const path = notePath(row.topic_id, row.board_norm, row.role_norm, row.note);
    await uploadLibraryFile(url, key, path, row.note);
    await patch(
      `library_board_notes?topic_id=eq.${encodeURIComponent(row.topic_id)}&board_norm=eq.${encodeURIComponent(row.board_norm)}&role_norm=eq.${encodeURIComponent(row.role_norm)}`,
      { note: null, note_path: path },
    );
    notes++;
  }
  console.log(`  recortes movidos: ${notes}/${notesLeft}`);
}

console.log(`Pronto: ${topics} matérias e ${notes} recortes agora são arquivos.`);
console.log("Para o banco devolver o espaço, rode no SQL Editor do Supabase:");
console.log("  vacuum full public.library_topics;");
console.log("  vacuum full public.library_board_notes;");
