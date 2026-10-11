// Confere arquivos da auditoria (só leitura). Uso:
// node --experimental-transform-types --no-warnings conteudo/entrada/auditoria-2026-10-11/_ferramentas/conferir.mts <arquivo.json>...
import { validateLibraryMaterial, validateBoardNote } from "../../../../supabase/functions/_shared/library.ts";
import fs from "node:fs";
const E = new URL("..", import.meta.url).pathname;
const probs = (fn: () => unknown): string[] => { try { fn(); return []; } catch (e: any) { return e.problems ?? [String(e.message)]; } };
const bias = (qs: any[]) => { const lg: number[] = [], sh: number[] = []; let mc = 0;
  qs.forEach((q, i) => { const o = q.options ?? []; if (q.format !== "MULTIPLE_CHOICE" || o.length < 4) return; const r = o.filter((x: any) => x.correct === true); if (r.length !== 1) return; mc++;
    const L = o.map((x: any) => String(x.text ?? "").length), rl = String(r[0].text).length;
    if (rl === Math.max(...L) && L.filter((l: number) => l === rl).length === 1) lg.push(i + 1);
    if (rl === Math.min(...L) && L.filter((l: number) => l === rl).length === 1) sh.push(i + 1); });
  return { mc, lg, sh, lim: Math.floor(mc * 0.4) }; };
for (const f of process.argv.slice(2)) {
  const d = JSON.parse(fs.readFileSync(f, "utf8")); const out: string[] = [];
  if (Array.isArray(d)) {
    const mat = JSON.parse(fs.readFileSync(`${E}/materias/${d[0].topic}.json`, "utf8"));
    for (const r of d) { out.push(...probs(() => validateBoardNote(r, mat)).map((p) => `[${r.board}] ${p}`));
      if (/j[aá] (cobrou|reuniu|explorou|exigiu|pediu|trouxe|apresentou)|h[aá] (prova|quest[aã]o) d[ae] banca|j[aá] em prova/i.test(r.howItFalls)) out.push(`[${r.board}] ainda afirma prova real`); }
  } else if (d.chapters) { out.push(...probs(() => validateLibraryMaterial(d))); const b = bias(d.questions); out.push(`viés: certa mais longa em ${b.lg.length}/${b.mc} [${b.lg}] | mais curta em ${b.sh.length}/${b.mc} [${b.sh}] | limite ${b.lim}`); }
  else { const qs = d.questions ?? []; const b = bias(qs);
    qs.forEach((q: any, i: number) => { const o = q.options ?? []; if (o.filter((x: any) => x.correct === true).length !== 1) out.push(`#${i + 1}: precisa de exatamente uma correta`);
      if (new Set(o.map((x: any) => String(x.text).trim().toLowerCase())).size !== o.length) out.push(`#${i + 1}: alternativas repetidas`);
      if (String(q.explanation ?? "").length < 120) out.push(`#${i + 1}: explicação < 120`); });
    out.push(`${qs.length} questões | viés: certa mais longa em ${b.lg.length}/${b.mc} [${b.lg}] | mais curta em ${b.sh.length}/${b.mc} [${b.sh}] | limite ${b.lim}`); }
  console.log(`== ${f.split("/").pop()}\n` + (out.length ? out.map((x) => "  " + x).join("\n") : "  ok"));
}
