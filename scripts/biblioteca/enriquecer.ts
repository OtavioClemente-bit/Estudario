// Junta a resposta de enriquecimento visual (capítulos + explicações) na matéria publicada.
// Confere: mesmos títulos, blocos grafico válidos, sem cifrão simples, e números das explicações preservados.
// deno run --allow-read --allow-write scripts/biblioteca/enriquecer.ts <resposta-visual.json> [--gravar]
type Json = Record<string, any>;
const [file, flag] = Deno.args;
const visual: Json = JSON.parse(Deno.readTextFileSync(file));
const [area, base] = String(visual.id).split(".");
const path = `conteudo/materias/${area}/${base}.json`;
const m: Json = JSON.parse(Deno.readTextFileSync(path));
const problems: string[] = [];
const warns: string[] = [];

const charts = (md: string) => [...md.matchAll(/```grafico\n([\s\S]*?)\n```/g)].map((x) => x[1]);
const singleDollar = (md: string) => md.replace(/\$\$[\s\S]*?\$\$/g, "").replace(/R\$/g, "").includes("$");
const numbers = (s: string) => new Set((s.replace(/\\[a-z]+/gi, " ").match(/\d+(?:[.,]\d+)?/g) ?? []).map((n) => n.replace(",", ".")));

if (!Array.isArray(visual.chapters) || visual.chapters.length !== m.chapters.length) problems.push(`capítulos: ${visual.chapters?.length} (esperado ${m.chapters.length})`);
let nCharts = 0;
visual.chapters?.forEach((c: Json, i: number) => {
  if (c.title !== m.chapters[i]?.title) problems.push(`capítulo ${i + 1} mudou o título: "${c.title}"`);
  const md = String(c.markdown ?? "");
  if (md.length < m.chapters[i].markdown.length * 0.8) problems.push(`capítulo ${i + 1} encolheu (${m.chapters[i].markdown.length} → ${md.length})`);
  for (const ch of charts(md)) { nCharts++; try { const j = JSON.parse(ch); if (!["pizza", "barras", "linha", "funcao", "geometria"].includes(j.tipo)) problems.push(`capítulo ${i + 1}: tipo de gráfico "${j.tipo}"`); } catch { problems.push(`capítulo ${i + 1}: gráfico com JSON inválido`); } }
  if (singleDollar(md)) problems.push(`capítulo ${i + 1}: cifrão simples`);
  const lost = [...numbers(m.chapters[i].markdown)].filter((n) => !numbers(md).has(n));
  if (lost.length) warns.push(`capítulo ${i + 1}: números do original que sumiram: ${lost.slice(0, 12).join(", ")}`);
});
const blocks = visual.chapters?.reduce((s: number, c: Json) => s + (String(c.markdown).match(/^\$\$\s*$/gm)?.length ?? 0) / 2, 0);

for (const e of visual.explanations ?? []) {
  const q = m.questions[e.index];
  if (!q) { problems.push(`explicação de índice ${e.index} inexistente`); continue; }
  const text = String(e.explanation);
  if (singleDollar(text)) problems.push(`questão ${e.index}: cifrão simples`);
  for (const ch of charts(text)) { try { JSON.parse(ch); } catch { problems.push(`questão ${e.index}: gráfico inválido`); } }
  const lost = [...numbers(q.explanation)].filter((n) => !numbers(text).has(n));
  if (lost.length > 2) warns.push(`questão ${e.index}: números que sumiram da explicação: ${lost.join(", ")}`);
  const key = q.options?.find((o: Json) => o.correct)?.key;
  const said = text.match(/(?:alternativa|letra|gabarito)\s*:?\s*([A-E])\b/i)?.[1];
  if (q.format === "MULTIPLE_CHOICE" && said && said !== key) problems.push(`questão ${e.index}: explicação aponta ${said}, gabarito ${key}`);
}

console.log(`${visual.id}: ${nCharts} gráficos/figuras, ${blocks} fórmulas em bloco, ${visual.explanations?.length ?? 0} explicações reformatadas`);
if (warns.length) console.log("  ? " + warns.join("\n  ? "));
if (problems.length) { console.log("  ✗ " + problems.join("\n  ✗ ")); Deno.exit(1); }
if (flag === "--gravar") {
  visual.chapters.forEach((c: Json, i: number) => (m.chapters[i].markdown = c.markdown));
  for (const e of visual.explanations ?? []) m.questions[e.index].explanation = e.explanation;
  Deno.writeTextFileSync(path, JSON.stringify(m, null, 2) + "\n");
  console.log(`  gravado em ${path}`);
}
