// Matéria do plano que já existe toma posse dos seus tópicos: tira esses apelidos das matérias antigas
// (as "pacote") e aponta o edital para ela. deno run -A scripts/biblioteca/plano-apelidos.ts
import { plano } from "../../conteudo/pedidos/_plano-trt3.ts";
import { normalizeAlias } from "../../supabase/functions/_shared/library.ts";
const M = "conteudo/materias";
const exists = (id: string) => { const [a, b] = id.split("."); try { Deno.statSync(`${M}/${a}/${b}.json`); return true; } catch { return false; } };
const owner = new Map<string, string>();
for (const m of plano) if (exists(m.id)) for (const t of [m.title, ...m.topics]) owner.set(normalizeAlias(t), m.id);
for (const a of Deno.readDirSync(M)) if (a.isDirectory) for (const e of Deno.readDirSync(`${M}/${a.name}`)) {
  const f = `${M}/${a.name}/${e.name}`; const d = JSON.parse(Deno.readTextFileSync(f));
  const keep = d.aliases.filter((x: string) => { const o = owner.get(normalizeAlias(x)); return !o || o === d.id; });
  if (keep.length !== d.aliases.length) { console.log(`${d.id}: -${d.aliases.length - keep.length} apelido(s)`); d.aliases = keep; Deno.writeTextFileSync(f, JSON.stringify(d, null, 2) + "\n"); }
}
let n = 0;
for (const e of Deno.readDirSync("conteudo/editais")) if (e.name.endsWith(".json")) {
  const p = `conteudo/editais/${e.name}`; const ed = JSON.parse(Deno.readTextFileSync(p)); let ch = false;
  for (const d of ed.disciplinas) for (const t of d.topicos) { const o = owner.get(normalizeAlias(t.texto)); if (o && t.topico !== o) { t.topico = o; ch = true; n++; } }
  if (ch) Deno.writeTextFileSync(p, JSON.stringify(ed, null, 2) + "\n");
}
console.log(`tópicos de edital reapontados: ${n}`);
