// Cópia local das tabelas da biblioteca (matérias com o JSON e recortes de banca), antes de mexer nelas.
//   deno run --allow-env --allow-net --allow-write scripts/biblioteca/backup-biblioteca.ts <pasta>
// Grava library_topics.jsonl e library_board_notes.jsonl (uma linha por registro, tudo o que o banco tem).

const url = Deno.env.get("SUPABASE_URL")?.replace(/\/$/, "");
const key = Deno.env.get("SUPABASE_SERVICE_ROLE_KEY");
const dir = Deno.args[0];
if (!url || !key || !dir) {
  console.error("Uso: SUPABASE_URL e SUPABASE_SERVICE_ROLE_KEY no ambiente, e a pasta de destino.");
  Deno.exit(1);
}
await Deno.mkdir(dir, { recursive: true });
const headers = { apikey: key, authorization: `Bearer ${key}` };

async function dump(table: string, order: string, pageSize: number) {
  const file = await Deno.open(`${dir}/${table}.jsonl`, { write: true, create: true, truncate: true });
  const enc = new TextEncoder();
  let total = 0;
  for (let from = 0; ; from += pageSize) {
    const response = await fetch(`${url}/rest/v1/${table}?select=*&order=${order}`, { headers: { ...headers, range: `${from}-${from + pageSize - 1}` } });
    if (!response.ok) throw new Error(`${table}: ${response.status} ${await response.text()}`);
    const rows = await response.json() as unknown[];
    for (const row of rows) await file.write(enc.encode(JSON.stringify(row) + "\n"));
    total += rows.length;
    if (rows.length < pageSize) break;
  }
  file.close();
  console.log(`${table}: ${total} linhas`);
  return total;
}

await dump("library_topics", "id", 20);
await dump("library_board_notes", "topic_id,board_norm,role_norm", 100);
