// Liga nos editais as matérias novas do Codex que eu revisei e publiquei em conteudo/materias.
// Textos usados: as decisões do Codex (conteudo/entrada/itens-sem-materia/*.json) e o título/apelidos da matéria.
// Cuidado com texto genérico ("Agentes políticos.", "Formas de provimento.", "E 11/2017."): ele aparece em
// editais de outros concursos que falam de outra coisa. Regra:
//   - texto que só aparece em editais de UM concurso: liga em todos eles;
//   - texto que aparece em vários concursos: só liga se tiver número ou palavra marcante do título da matéria;
//     senão não liga e mostra o texto para eu conferir.
//   deno run --allow-read --allow-write scripts/codex/ligar-decisoes.ts --ids=area.arquivo,... [--simular]
import { normalizeAlias } from "../../supabase/functions/_shared/library.ts";
const ITENS = "conteudo/entrada/itens-sem-materia";
const arg = Deno.args.find((a) => a.startsWith("--ids="));
if (!arg) { console.error("passe --ids=a,b,c"); Deno.exit(1); }
const ids = new Set(arg.slice(6).split(",").filter(Boolean));
const simular = Deno.args.includes("--simular");
const norm = (s: string) => s.normalize("NFD").replace(/[̀-ͯ]/g, "").toLowerCase();
const VAZIAS = new Set("para com sobre entre como pela pelo pelos pelas dos das nos nas aos uma que lei estado estadual municipal municipio publica publico servico servidor educacao gestao direito nocoes geral basica ensino".split(" "));
const ler = (p: string) => JSON.parse(Deno.readTextFileSync(p).replace(/^﻿/, ""));

// Matéria de cada id, palavras marcantes do título.
const materia = new Map<string, Record<string, any>>();
for (const id of ids) {
  const [a, ...b] = id.split(".");
  materia.set(id, ler(`conteudo/materias/${a}/${b.join(".")}.json`));
}
const marcantes = (id: string) => new Set(norm(`${materia.get(id)!.title} ${id}`).split(/[^a-z0-9]+/).filter((w) => w.length >= 4 && !VAZIAS.has(w)));

// Exemplo de edital de cada texto da lista.
const exemplo = new Map<string, string>();
for (const l of Deno.readTextFileSync("conteudo/fila/itens-sem-materia.tsv").split("\n").slice(1)) {
  const c = l.split("\t");
  if (c[1]) exemplo.set(normalizeAlias(c[1]), c[3] ?? "");
}

const texto = new Map<string, string>(); // texto normalizado -> id
for (const e of Deno.readDirSync(ITENS)) {
  if (!e.name.endsWith(".json")) continue;
  let j: Record<string, any>;
  try { j = ler(`${ITENS}/${e.name}`); } catch { continue; }
  if (["ligar", "materia-nova"].includes(j.decisao) && ids.has(j.id) && j.texto) texto.set(normalizeAlias(String(j.texto)), j.id);
}
for (const [id, m] of materia) for (const a of [m.title, ...(m.aliases ?? [])]) if (!texto.has(normalizeAlias(a))) texto.set(normalizeAlias(a), id);

// Onde cada texto aparece sem matéria.
const concurso = (arq: string) => arq.split("--")[0];
const onde = new Map<string, Set<string>>();
const editais = [...Deno.readDirSync("conteudo/editais")].filter((e) => e.name.endsWith(".json")).map((e) => e.name);
const dados = new Map<string, Record<string, any>>();
for (const arq of editais) {
  const ed = ler(`conteudo/editais/${arq}`);
  dados.set(arq, ed);
  for (const d of ed.disciplinas) for (const t of d.topicos) {
    if (t.topico != null) continue;
    const k = normalizeAlias(String(t.texto));
    if (texto.has(k)) { const s = onde.get(k) ?? new Set(); s.add(concurso(arq)); onde.set(k, s); }
  }
}
const permitido = new Map<string, Set<string> | "todos">();
const conferir: string[] = [];
for (const [k, conc] of onde) {
  const id = texto.get(k)!;
  const palavras = norm(k).split(/[^a-z0-9]+/);
  const especifico = /\d/.test(k) && !/^e?\s*\d/.test(norm(k)) || palavras.some((w) => marcantes(id).has(w));
  if (conc.size === 1 || especifico) { permitido.set(k, "todos"); continue; }
  const ex = exemplo.get(k);
  permitido.set(k, new Set()); // genérico em vários concursos: não liga (o exemplo da lista pode ser de outro concurso)
  conferir.push(`${id} | "${k}" aparece em ${conc.size} concursos; não ligado (exemplo da lista: ${ex ?? "nenhum"})`);
}
const bloqueados = new Set((ler("conteudo/pedidos/nao-ligar.json").textos as string[]));
let total = 0;
const porId = new Map<string, number>();
for (const [arq, ed] of dados) {
  let n = 0;
  for (const d of ed.disciplinas) for (const t of d.topicos) {
    if (t.topico != null || bloqueados.has(t.texto)) continue;
    const k = normalizeAlias(String(t.texto));
    const p = permitido.get(k);
    if (!p || (p !== "todos" && !p.has(concurso(arq)))) continue;
    t.topico = texto.get(k); n++;
    porId.set(t.topico, (porId.get(t.topico) ?? 0) + 1);
  }
  if (n && !simular) Deno.writeTextFileSync(`conteudo/editais/${arq}`, JSON.stringify(ed, null, 2) + "\n");
  total += n;
}
for (const [id, n] of [...porId].sort((a, b) => b[1] - a[1])) console.log(`${String(n).padStart(5)}  ${id}`);
console.log(`\n${total} itens de edital ligados${simular ? " (simulação)" : ""}`);
if (conferir.length) console.log(`\nTextos genéricos que ficaram sem ligar:\n${conferir.join("\n")}`);
