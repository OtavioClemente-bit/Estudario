// Revisão por amostra das matérias do Codex (conteudo/entrada/materias-codex-2) aprovadas no conferidor e
// ainda não publicadas. Só escolhe o que mostrar ao revisor e copia as aprovadas; não escreve conteúdo.
//   deno run -A scripts/codex/amostra-materias.ts --montar     → work/amostra-codex.json (lotes de 25)
//   deno run -A scripts/codex/amostra-materias.ts --lote=N     → imprime teoria resumida + 6 questões de cada
//   deno run -A scripts/codex/amostra-materias.ts --aplicar    → publica as "ok" de work/amostra-codex-res/*.json
//     (copia para conteudo/materias/<área>/<resto>.json com status PUBLISHED e liga as decisões)
const ROOT = new URL("../../", import.meta.url).pathname.replace(/^\/([A-Za-z]:)/, "$1");
const PASTA = `${ROOT}conteudo/entrada/materias-codex-2`;
const LOTES = `${ROOT}work/amostra-codex.json`;
const RES = `${ROOT}work/amostra-codex-res`;
const POR_LOTE = 25, POR_MATERIA = 6;
type Json = Record<string, any>;
const destino = (id: string) => id.includes(".") ? `${ROOT}conteudo/materias/${id.split(".")[0]}/${id.split(".").slice(1).join(".")}.json` : null;
const existe = (id: string) => { try { Deno.statSync(`${PASTA}/${id}.json`); return true; } catch { return false; } };
const publicada = (id: string) => { const d = destino(id); if (!d) return false; try { Deno.statSync(d); return true; } catch { return false; } };
const aprovada = (arquivo: string) => {
  const o = new Deno.Command("deno", { args: ["run", "--allow-read", "--allow-env", `${ROOT}scripts/biblioteca/conferir-materia.ts`, arquivo, "--estrito"], stdout: "piped", stderr: "null" }).outputSync();
  return new TextDecoder().decode(o.stdout).trim().endsWith("OK");
};

// Reprovadas na revisão (work/codex-reprovadas.json, id → hora do arquivo quando foi reprovado): só voltam à amostra
// se o rascunho mudar depois disso (09/11: 5 reprovadas de 08/11 voltaram iguais, porque o arquivo continuava na pasta).
const REPROVADAS = `${ROOT}work/codex-reprovadas.json`;
let reprovadas: Record<string, number> = {};
try { reprovadas = JSON.parse(Deno.readTextFileSync(REPROVADAS)); } catch { /* nenhuma */ }
const mtime = (id: string) => { try { return Deno.statSync(`${PASTA}/${id}.json`).mtime?.getTime() ?? 0; } catch { return 0; } };

let duplicadasMontar = new Set<string>();
try { duplicadasMontar = new Set(Deno.readTextFileSync(`${ROOT}work/duplicadas.txt`).split(/\s+/).filter(Boolean)); } catch { /* nenhuma */ }

if (Deno.args.includes("--montar")) {
  const ids: string[] = [];
  for (const e of [...Deno.readDirSync(PASTA)].sort((a, b) => a.name.localeCompare(b.name))) {
    if (!e.name.endsWith(".json")) continue;
    const id = e.name.replace(/\.json$/, "");
    if (!destino(id) || publicada(id)) continue;
    if (reprovadas[id] && mtime(id) <= reprovadas[id]) continue;
    // Duplicada de matéria já publicada (work/duplicadas.txt): o --aplicar não copia, então nem vai para a amostra (09/11).
    if (duplicadasMontar.has(id)) continue;
    if (aprovada(`${PASTA}/${e.name}`)) ids.push(id);
  }
  const lotes = [];
  for (let i = 0; i < ids.length; i += POR_LOTE) lotes.push(ids.slice(i, i + POR_LOTE));
  Deno.mkdirSync(RES, { recursive: true });
  Deno.writeTextFileSync(LOTES, JSON.stringify(lotes));
  console.log(`${ids.length} matérias em ${lotes.length} lotes`);
} else if (Deno.args.some((a) => a.startsWith("--lote="))) {
  const n = Number(Deno.args.find((a) => a.startsWith("--lote="))!.slice(7));
  for (const id of JSON.parse(Deno.readTextFileSync(LOTES))[n - 1] ?? []) {
    const m: Json = JSON.parse(Deno.readTextFileSync(`${PASTA}/${id}.json`));
    console.log(`\n### ${id} — ${m.title}\nescopo: ${JSON.stringify(m.scope ?? "").slice(0, 300)}`);
    console.log(`capítulos: ${(m.chapters ?? []).map((c: Json) => c.title).join(" | ")}`);
    for (const c of (m.chapters ?? []).slice(0, 2)) console.log(`[${c.title}] ${String(c.markdown ?? "").replace(/\s+/g, " ").slice(0, 700)}`);
    const qs: Json[] = m.questions ?? [];
    const pool = qs.map((_, i) => i);
    for (let i = pool.length - 1; i > 0; i--) { const k = Math.floor(Math.random() * (i + 1)); [pool[i], pool[k]] = [pool[k], pool[i]]; }
    for (const i of pool.slice(0, POR_MATERIA).sort((a, b) => a - b)) {
      const q = qs[i];
      console.log(`[q${i}] ${q.statement}`);
      for (const o of q.options ?? []) console.log(`   ${o.key}) ${o.text}${o.correct ? "  <-- gabarito" : ""}`);
      console.log(`   explicação: ${q.explanation}`);
    }
  }
} else if (Deno.args.includes("--aplicar")) {
  const ok: string[] = [];
  // Duplicadas de matéria já publicada (achadas pela colisão de apelido): não voltam a ser copiadas.
  let duplicadas = new Set<string>();
  try { duplicadas = new Set(Deno.readTextFileSync(`${ROOT}work/duplicadas.txt`).split(/\s+/).filter(Boolean)); } catch { /* nenhuma */ }
  for (const e of Deno.readDirSync(RES)) {
    if (!e.name.endsWith(".json")) continue;
    const r = JSON.parse(Deno.readTextFileSync(`${RES}/${e.name}`));
    for (const id of r.ok ?? []) if (!publicada(id) && destino(id) && !duplicadas.has(id) && existe(id)) ok.push(id);
    for (const id of Object.keys(r.reprovar ?? {})) reprovadas[id] = mtime(id);
  }
  // Uma "ok" numa conferência posterior (ler-N.json) vence a reprovação anterior.
  for (const id of ok) delete reprovadas[id];
  Deno.writeTextFileSync(REPROVADAS, JSON.stringify(reprovadas, null, 1));
  for (const id of ok) {
    const m = JSON.parse(Deno.readTextFileSync(`${PASTA}/${id}.json`));
    m.status = "PUBLISHED";
    Deno.mkdirSync(destino(id)!.replace(/\/[^/]+$/, ""), { recursive: true });
    Deno.writeTextFileSync(destino(id)!, JSON.stringify(m, null, 2) + "\n");
  }
  if (ok.length) {
    // Com centenas de ids a linha de comando passava do limite do Windows (07/11): a lista vai num arquivo.
    Deno.writeTextFileSync(`${ROOT}work/amostra-codex-ok-ids.txt`, ok.join("\n") + "\n");
    const o = new Deno.Command("deno", { args: ["run", "-A", `${ROOT}scripts/codex/ligar-decisoes.ts`, `--ids=@${ROOT}work/amostra-codex-ok-ids.txt`], stdout: "piped", stderr: "piped" }).outputSync();
    console.log(new TextDecoder().decode(o.stdout).trim().split("\n").slice(-3).join("\n"));
  }
  console.log(`${ok.length} matérias copiadas para conteudo/materias`);
}
