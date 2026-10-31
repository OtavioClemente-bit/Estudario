// Preenche library_board_notes.question_count a partir dos recortes locais (conteudo/recortes), para o
// painel Equipe › Cobertura somar as questões dos recortes. O publicador (importar.ts) já grava a
// contagem dos recortes que envia; este script é para os que foram publicados antes da coluna existir.
// Uso: bash scripts/biblioteca/com-chave.sh scripts/biblioteca/contar-recortes.ts
import { normalizeAlias, normalizeBoard } from "../../supabase/functions/_shared/library.ts";

type Json = Record<string, unknown>;
const url = Deno.env.get("SUPABASE_URL")!.replace(/\/$/, "");
const key = Deno.env.get("SUPABASE_SERVICE_ROLE_KEY")!;
const headers = { apikey: key, authorization: `Bearer ${key}`, "content-type": "application/json", prefer: "return=minimal" };

async function* walk(dir: string): AsyncGenerator<string> {
  for await (const entry of Deno.readDir(dir)) {
    const path = `${dir}/${entry.name}`;
    if (entry.isDirectory) yield* walk(path);
    else if (entry.name.endsWith(".json")) yield path;
  }
}

let done = 0;
let failed = 0;
const pending: Promise<void>[] = [];
for await (const path of walk("conteudo/recortes")) {
  const value = JSON.parse(await Deno.readTextFile(path)) as Json;
  const count = Array.isArray(value.questions) ? value.questions.length : 0;
  const filter = `topic_id=eq.${encodeURIComponent(String(value.topic))}&board_norm=eq.${encodeURIComponent(normalizeBoard(String(value.board)))}` +
    `&role_norm=eq.${encodeURIComponent(normalizeAlias(value.role as string | null))}`;
  pending.push(
    fetch(`${url}/rest/v1/library_board_notes?${filter}`, { method: "PATCH", headers, body: JSON.stringify({ question_count: count }) })
      .then(async (r) => { await r.body?.cancel(); if (r.ok) done++; else failed++; }),
  );
  // Dez de cada vez, para não sobrecarregar o banco do plano grátis.
  if (pending.length >= 10) { await Promise.all(pending); pending.length = 0; }
}
await Promise.all(pending);
console.log(`${done} recorte(s) com a contagem gravada; ${failed} falha(s).`);
