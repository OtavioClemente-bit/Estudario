// Conferidor de matéria: deno run --allow-read conteudo/fila/conferir.ts <arquivo.json>. Só lê; não altera nada.
import { validateLibraryMaterial } from "../../supabase/functions/_shared/library.ts";
const path = Deno.args[0];
const raw = Deno.readTextFileSync(path);
let m: any;
try { m = JSON.parse(raw); } catch (e) { console.log("JSON INVÁLIDO:", (e as Error).message); Deno.exit(1); }
try { validateLibraryMaterial(m); console.log("validador: OK"); }
catch (e: any) { console.log("validador:", e.problems ?? e.message); }
const qs = m.questions ?? [];
const mc = qs.filter((q: any) => q.format === "MULTIPLE_CHOICE");
const letters: Record<string, number> = {};
let longest = 0;
mc.forEach((q: any) => {
  const r = q.options.findIndex((o: any) => o.correct); const k = q.options[r]?.key; letters[k] = (letters[k] ?? 0) + 1;
  const s = q.options.map((o: any) => o.text.length);
  if (s[r] === Math.max(...s) && s.filter((x: number) => x === s[r]).length === 1) longest++;
});
const diff: Record<string, number> = {};
qs.forEach((q: any) => diff[q.difficulty] = (diff[q.difficulty] ?? 0) + 1);
console.log(`questões=${qs.length} ME=${mc.length} CE=${qs.length - mc.length} dif=${JSON.stringify(diff)} gabarito=${JSON.stringify(letters)} certa+longa=${longest}/${mc.length}`);
qs.forEach((q: any, i: number) => {
  const nc = q.options.filter((o: any) => o.correct).length;
  if (nc !== 1) console.log(`q${i}: ${nc} corretas`);
  if (q.board != null || q.agency != null) console.log(`q${i}: board/agency preenchido`);
  q.options.forEach((o: any) => { if (/[^.]\.\s+[a-zà-ú]/.test(o.text)) console.log(`q${i}: trecho colado: ${o.text}`); });
  if (q.format === "TRUE_FALSE" && /alternativ|opç|opc/i.test(q.explanation)) console.log(`q${i}: C/E fala de alternativa`);
});
(m.aliases ?? []).forEach((a: string) => { if (a.trim().length < 10) console.log(`apelido curto: "${a}"`); });
const proib = /\b(fumarc|idecan|cespe|cebraspe|fgv|vunesp|banca|trt|cbmmg|tribunal regional|bombeir\w*|comandante|guarni\w*|sargento|viatura|quartel|patrulh\w*|socorrist\w*|militar\w*|corpora\w*)\b/gi;
const hits = new Map<string, string[]>();
const walk = (v: any, p: string) => {
  if (typeof v === "string") { for (const x of v.matchAll(proib)) { const l = hits.get(x[0].toLowerCase()) ?? []; l.push(p); hits.set(x[0].toLowerCase(), l); } }
  else if (Array.isArray(v)) v.forEach((x, i) => walk(x, `${p}[${i}]`));
  else if (v && typeof v === "object") for (const k of Object.keys(v)) if (k !== "sources") walk(v[k], `${p}.${k}`);
};
walk(m, "");
for (const [w, ps] of hits) console.log(`PROIBIDA "${w}" x${ps.length}: ${ps.slice(0, 8).join(" ")}`);
