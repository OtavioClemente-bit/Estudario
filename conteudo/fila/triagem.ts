// Triagem de conteudo/entrada (só lê): deno run --allow-read conteudo/fila/triagem.ts
// Classifica cada matéria em OK / CONSERTAR / REFAZER e aponta apelidos que já pertencem à biblioteca.
import { validateLibraryMaterial } from "../../supabase/functions/_shared/library.ts";

const root = new URL("../", import.meta.url).pathname.replace(/^\/([A-Za-z]:)/, "$1");
const read = (p: string) => JSON.parse(Deno.readTextFileSync(p).replace(/^﻿/, ""));
const walk = (dir: string, out: string[] = []) => {
  for (const e of Deno.readDirSync(dir)) {
    const p = `${dir}/${e.name}`;
    if (e.isDirectory) walk(p, out); else if (e.name.endsWith(".json")) out.push(p);
  }
  return out;
};
const norm = (s: string) => s.normalize("NFD").replace(/[̀-ͯ]/g, "").toLowerCase().replace(/[^a-z0-9]+/g, " ").trim();

// Biblioteca publicada: id e apelidos.
const libIds = new Map<string, string>();
const libAlias = new Map<string, string>();
for (const p of walk(`${root}materias`)) {
  try { const m = read(p); libIds.set(m.id, p); for (const a of m.aliases ?? []) libAlias.set(norm(a), m.id); } catch { /* ignora */ }
}

const rows: string[] = [];
const summary = { OK: 0, CONSERTAR: 0, REFAZER: 0, RECORTE: 0, INVALIDO: 0 };
for (const e of [...Deno.readDirSync(`${root}entrada`)].sort((a, b) => a.name.localeCompare(b.name))) {
  if (!e.name.endsWith(".json")) continue;
  const path = `${root}entrada/${e.name}`;
  const raw = Deno.readTextFileSync(path);
  let m: any;
  try { m = JSON.parse(raw.replace(/^﻿/, "")); } catch (err) { summary.INVALIDO++; rows.push(`INVALIDO\t${e.name}\tJSON: ${(err as Error).message.slice(0, 80)}`); continue; }
  if (!Array.isArray(m.questions)) { summary.RECORTE++; rows.push(`RECORTE\t${e.name}\t${m.topic ?? m.matterId ?? ""} · ${m.board ?? ""}`); continue; }
  if (!m.questions.every((q: any) => Array.isArray(q?.options))) { summary.INVALIDO++; rows.push(`INVALIDO	${e.name}	formato diferente (options não é lista): ${Object.keys(m.questions[0] ?? {}).join(",")}`); continue; }
  const problems: string[] = [];
  let fatal = false;
  try { validateLibraryMaterial(m); } catch (err: any) { const p = err.problems ?? [err.message]; problems.push(`validador(${p.length}): ${String(p[0]).slice(0, 90)}`); if (p.length > 6) fatal = true; }
  const qs = m.questions;
  const mc = qs.filter((q: any) => q.format === "MULTIPLE_CHOICE");
  let longest = 0; const letters: Record<string, number> = {};
  for (const q of mc) {
    const i = q.options.findIndex((o: any) => o.correct); const k = q.options[i]?.key; letters[k] = (letters[k] ?? 0) + 1;
    const s = q.options.map((o: any) => o.text.length);
    if (s[i] === Math.max(...s) && s.filter((x: number) => x === s[i]).length === 1) longest++;
  }
  const multi = qs.filter((q: any) => q.options.filter((o: any) => o.correct).length !== 1).length;
  const twoRight = qs.filter((q: any) => /tamb[ée]m (est[áa]|[ée]) corret|duas (alternativas )?corretas|tamb[ée]m seria correta/i.test(q.explanation ?? "")).length;
  const maxLetter = Math.max(0, ...Object.values(letters));
  if (qs.length < 30) { problems.push(`só ${qs.length} questões`); fatal = qs.length < 15; }
  if (multi) { problems.push(`${multi} com ≠1 correta`); }
  if (twoRight) problems.push(`${twoRight} explicação admite outra certa`);
  if (mc.length && longest / mc.length > 0.6) problems.push(`certa mais longa ${longest}/${mc.length}`);
  if (mc.length && maxLetter / mc.length > 0.4) problems.push(`gabarito concentrado ${JSON.stringify(letters)}`);
  if (m.status !== "PUBLISHED") problems.push(`status ${m.status}`);
  if (raw.charCodeAt(0) === 0xfeff) problems.push("BOM");
  const dup = libIds.has(m.id) ? "JÁ PUBLICADA (mesmo id)" : "";
  const collisions = (m.aliases ?? []).filter((a: string) => libAlias.has(norm(a)) && libAlias.get(norm(a)) !== m.id).map((a: string) => `${a}→${libAlias.get(norm(a))}`);
  if (collisions.length) problems.push(`apelido já usado: ${collisions.slice(0, 2).join("; ")}${collisions.length > 2 ? "…" : ""}`);
  const verdict = fatal ? "REFAZER" : problems.filter((p) => !p.startsWith("status") && p !== "BOM").length ? "CONSERTAR" : "OK";
  summary[verdict]++;
  rows.push(`${verdict}\t${e.name}\t${m.id} · ${qs.length}q ${dup}\t${problems.join(" | ")}`);
}
console.log(rows.join("\n"));
console.log("\nRESUMO", JSON.stringify(summary));
