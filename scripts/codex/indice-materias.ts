// Regera conteudo/fila/indice-materias.tsv (id, título, o que cobre), que o Codex consulta antes de decidir
// "ligar" ou "materia-nova". Entram as matérias publicadas, os rascunhos já escritos e os ids de matéria nova já
// decididos e ainda não escritos ("em produção"), para ele não criar de novo o que já existe ou já foi pedido.
// O índice ficou parado em 28/10 (1.259 matérias de 4.500) e o Codex criava duplicatas (04/11).
//   deno run -A scripts/codex/indice-materias.ts
const ROOT = new URL("../../", import.meta.url).pathname.replace(/^\/([A-Za-z]:)/, "$1");
type Json = Record<string, any>;
const limpa = (s: unknown) => String(s ?? "").replace(/\s+/g, " ").replace(/\t/g, " ").trim();
const linhas = new Map<string, string>();
const cobre = (m: Json) => limpa(Array.isArray(m.scope?.covers) ? m.scope.covers.join("; ") : (m.chapters ?? []).map((c: Json) => String(c.title).replace(/^\d+\.\s*/, "")).join("; ")).slice(0, 400);
for (const a of Deno.readDirSync(`${ROOT}conteudo/materias`)) {
  if (!a.isDirectory) continue;
  for (const f of Deno.readDirSync(`${ROOT}conteudo/materias/${a.name}`)) {
    if (!f.name.endsWith(".json")) continue;
    try { const m = JSON.parse(Deno.readTextFileSync(`${ROOT}conteudo/materias/${a.name}/${f.name}`)); if (m.status === "PUBLISHED") linhas.set(m.id, `${m.id}\t${limpa(m.title)}\t${cobre(m)}`); } catch { /* ignora */ }
  }
}
for (const f of Deno.readDirSync(`${ROOT}conteudo/entrada/materias-codex-2`)) {
  if (!f.name.endsWith(".json")) continue;
  const id = f.name.slice(0, -5);
  if (linhas.has(id)) continue;
  try { const m = JSON.parse(Deno.readTextFileSync(`${ROOT}conteudo/entrada/materias-codex-2/${f.name}`)); linhas.set(id, `${id}\t${limpa(m.title)} (rascunho)\t${cobre(m)}`); } catch { /* ignora */ }
}
for (const f of Deno.readDirSync(`${ROOT}conteudo/entrada/itens-sem-materia`)) {
  if (!/^\d+\.json$/.test(f.name)) continue;
  try {
    const j = JSON.parse(Deno.readTextFileSync(`${ROOT}conteudo/entrada/itens-sem-materia/${f.name}`));
    if (j.decisao === "materia-nova" && j.id && !linhas.has(j.id)) linhas.set(j.id, `${j.id}\t(em produção)\t${limpa(j.texto).slice(0, 300)}`);
  } catch { /* ignora */ }
}
// O Codex pode estar com o arquivo aberto (o Windows recusa a escrita): grava ao lado e troca, tentando de novo.
const destino = `${ROOT}conteudo/fila/indice-materias.tsv`;
Deno.writeTextFileSync(`${destino}.novo`, ["id\ttítulo\to que cobre", ...[...linhas.values()].sort()].join("\n") + "\n");
for (let t = 0; ; t++) {
  try { Deno.renameSync(`${destino}.novo`, destino); break; } catch (e) { if (t >= 20) throw e; await new Promise((r) => setTimeout(r, 3000)); }
}
console.log(`${linhas.size} matérias no índice`);
