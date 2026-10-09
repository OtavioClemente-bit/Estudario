// Liga os tópicos de edital (topico: null) à matéria cujo apelido é exatamente o texto do tópico.
// deno run --allow-read --allow-write scripts/biblioteca/ligar-editais.ts
import { normalizeAlias } from "../../supabase/functions/_shared/library.ts";
const owner = new Map<string, string>();
for (const area of Deno.readDirSync("conteudo/materias")) {
  if (!area.isDirectory) continue;
  for (const e of Deno.readDirSync(`conteudo/materias/${area.name}`)) {
    const m = JSON.parse(Deno.readTextFileSync(`conteudo/materias/${area.name}/${e.name}`));
    for (const a of [m.title, ...(m.aliases ?? [])]) owner.set(normalizeAlias(a), m.id);
  }
}
// Textos que não podem ser ligados (lei estadual x federal, versão de software diferente).
const bloqueados = new Set((JSON.parse(Deno.readTextFileSync("conteudo/pedidos/nao-ligar.json")).textos as string[]));
let linked = 0;
for (const e of Deno.readDirSync("conteudo/editais")) {
  if (!e.name.endsWith(".json")) continue;
  const path = `conteudo/editais/${e.name}`;
  const ed = JSON.parse(Deno.readTextFileSync(path));
  let n = 0;
  for (const d of ed.disciplinas) for (const t of d.topicos) {
    if (t.topico != null || bloqueados.has(t.texto)) continue;
    const id = owner.get(normalizeAlias(t.texto));
    if (id) { t.topico = id; n++; }
  }
  if (n) { Deno.writeTextFileSync(path, JSON.stringify(ed, null, 2) + "\n"); console.log(`${e.name}: +${n}`); linked += n; }
}
console.log(`ligados: ${linked}`);
