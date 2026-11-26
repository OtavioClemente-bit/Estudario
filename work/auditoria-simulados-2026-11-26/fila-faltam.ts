import { normalizeBoard } from "../../supabase/functions/_shared/library.ts";
const D = "work/auditoria-simulados-2026-11-26/";
const f: Record<string, string[]> = {};
for (const e of Deno.readDirSync(D)) if (/^faltam-topicos-provas.*\.json$/.test(e.name)) Object.assign(f, JSON.parse(Deno.readTextFileSync(D + e.name)));
const out: { prova: string; disciplina: string; materia: string; recorte: string }[] = [];
for (const [prova, lst] of Object.entries(f)) {
  const sim = JSON.parse(Deno.readTextFileSync(`conteudo/simulados/${prova}.json`));
  const b = normalizeBoard(String(sim.banca ?? ""));
  for (const x of lst) { const [disciplina, materia] = x.split("|"); out.push({ prova, disciplina, materia, recorte: `${materia}|${b}` }); }
}
Deno.writeTextFileSync(D + "faltam-topicos.json", JSON.stringify(out, null, 1));
const P = "work/prioridade-recortes.json";
const pri = JSON.parse(Deno.readTextFileSync(P)) as string[];
const novos = [...new Set(out.map((o) => o.recorte))].filter((k) => !pri.includes(k));
Deno.writeTextFileSync(P, JSON.stringify([...pri, ...novos], null, 1) + "\n");
console.log(out.length, "tópicos;", novos.length, "recortes novos na fila de prioridade");
