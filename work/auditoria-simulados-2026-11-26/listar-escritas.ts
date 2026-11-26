import { alternativasCertas, normKey, topicoCabe, Usadas } from "../../scripts/simuladao/regras.ts";
const D = "work/auditoria-simulados-2026-11-26/";
const ids: string[] = [];
for (let i = 1; i <= 6; i++) ids.push(...JSON.parse(Deno.readTextFileSync(`${D}conserto-${i}de6.json`)).escritas.map((x: string) => x.split(" ")[0]));
const all = [...Deno.readDirSync("conteudo/simulados")].filter((e) => e.name.endsWith(".json") && e.name.includes(".simulado-")).map((e) => ({ id: e.name.replace(/\.json$/, ""), j: JSON.parse(Deno.readTextFileSync("conteudo/simulados/" + e.name)) }));
const out: any[] = [];
for (const id of ids) {
  const s = all.find((x) => x.id === id)!;
  const ed = JSON.parse(Deno.readTextFileSync(`conteudo/editais/${s.j.edital}.json`));
  const others = new Usadas(); for (const o of all) if (o.id !== id && o.j.edital === s.j.edital && o.j.status !== "DRAFT") for (const q of o.j.questions) others.add(q);
  const seen = new Set<string>();
  for (const q of s.j.questions) {
    const why: string[] = [];
    const fmt = q.format === "CERTO_ERRADO" ? "CERTO_ERRADO" : "MULTIPLE_CHOICE";
    if (!alternativasCertas(q, fmt)) why.push(`alternativas(${q.options.length})`);
    const k = normKey(q.statement); if (seen.has(k)) why.push("dup na prova"); seen.add(k);
    if (others.has(q)) why.push("repetida em outra prova");
    const disc = ed.disciplinas.find((d: any) => d.nome === q.disciplina);
    if (disc && q.materia) { const t = disc.topicos.filter((t: any) => t.topico === q.materia).map((t: any) => t.texto).join(" "); if (!topicoCabe(disc.nome, t, q.materia)) why.push("disciplina?"); }
    if (why.length) out.push({ id, n: q.n, why: why.join(","), disc: q.disciplina, mat: q.materia, st: String(q.statement).slice(0, 110), discs: ed.disciplinas.map((d: any) => d.nome) });
  }
}
Deno.writeTextFileSync(D + "escritas-flag.json", JSON.stringify(out, null, 1));
const by: Record<string, number> = {}; for (const o of out) by[o.why] = (by[o.why] || 0) + 1; console.log(out.length, by);
