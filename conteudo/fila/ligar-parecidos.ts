// Liga tópicos de edital a matérias por semelhança (Jaccard >= 0.6). Sem --gravar só mostra; revise os de 0.6–0.8.
// deno run --allow-read [--allow-write] conteudo/fila/ligar-parecidos.ts [--gravar] <arquivo-do-edital.json>...
import { normalizeAlias } from "../../supabase/functions/_shared/library.ts";
const stop = new Set("a o e de da do das dos em na no nas nos para por com sem sua seu suas seus ao aos as os um uma lei nº n e ou que se conceito conceitos nocoes noções".split(" "));
const tok = (s: string) => new Set(normalizeAlias(s).split(" ").filter((w) => w.length > 2 && !stop.has(w)));
const mats: { id: string; sets: Set<string>[] }[] = [];
for (const area of Deno.readDirSync("conteudo/materias")) if (area.isDirectory) for (const e of Deno.readDirSync(`conteudo/materias/${area.name}`)) {
  const m = JSON.parse(Deno.readTextFileSync(`conteudo/materias/${area.name}/${e.name}`));
  mats.push({ id: m.id, sets: [m.title, ...(m.aliases ?? [])].map(tok).filter((s) => s.size >= 2) });
}
const jac = (a: Set<string>, b: Set<string>) => { let i = 0; for (const x of a) if (b.has(x)) i++; return i / (a.size + b.size - i); };
const write = Deno.args[0] === "--gravar";
const only = Deno.args.filter((a) => a.endsWith(".json"));
let n = 0;
for (const f of only) {
  const p = `conteudo/editais/${f}`; const ed = JSON.parse(Deno.readTextFileSync(p)); let k = 0;
  for (const d of ed.disciplinas) for (const t of d.topicos) {
    if (t.topico != null) continue;
    const s = tok(t.texto); if (s.size < 2) continue;
    let best = 0, id = "";
    for (const m of mats) for (const a of m.sets) { const j = jac(s, a); if (j > best) { best = j; id = m.id; } }
    if (best >= 0.6) { console.log(`${best.toFixed(2)}  ${t.texto}  →  ${id}`); if (write) { t.topico = id; k++; } }
  }
  if (write && k) Deno.writeTextFileSync(p, JSON.stringify(ed, null, 2) + "\n");
  n += k;
}
if (write) console.log("gravados", n);
