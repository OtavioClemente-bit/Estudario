// Fase 2 dos itens sem matéria: o Codex decidiu "materia-nova" em vários itens mas não escreveu as matérias.
// Este programa pede ao Codex UMA matéria por rodada e só a considera feita quando o arquivo existe e o
// conferidor de matéria aprova. Repete até todas existirem; espera quando o limite do plano acaba.
//   deno run -A scripts/codex/loop-materias.ts
const ROOT = new URL("../../", import.meta.url).pathname.replace(/^\/([A-Za-z]:)/, "$1");
const CODEX_JS = `${Deno.env.get("APPDATA")}/npm/node_modules/@openai/codex/bin/codex.js`;
const ITENS = `${ROOT}conteudo/entrada/itens-sem-materia`;
const SAIDA = `${ROOT}conteudo/entrada/materias-codex-2`;
const LOG = `${ROOT}conteudo/entrada/materias-codex-2-log.txt`;
const sleep = (ms: number) => new Promise((r) => setTimeout(r, ms));
const log = (m: string) => {
  const l = `[${new Date().toLocaleString("pt-BR")}] ${m}`;
  console.log(l);
  Deno.writeTextFileSync(LOG, l + "\n", { append: true });
};
type Json = Record<string, any>;
const ler = (p: string) => JSON.parse(Deno.readTextFileSync(p).replace(/^﻿/, ""));

// Matérias novas declaradas, com os itens de edital que cada uma deve cobrir.
function pendentes(): { id: string; itens: string[] }[] {
  const porId = new Map<string, string[]>();
  for (const e of Deno.readDirSync(ITENS)) {
    if (!e.name.endsWith(".json")) continue;
    const j = ler(`${ITENS}/${e.name}`);
    if (j.decisao === "materia-nova" || j.decisao === "ligar") {
      if (j.decisao === "materia-nova" || porId.has(j.id)) porId.set(j.id, [...(porId.get(j.id) ?? []), String(j.texto)]);
    }
  }
  const existe = (id: string) => {
    const [a, ...b] = id.split(".");
    try { Deno.statSync(`${ROOT}conteudo/materias/${a}/${b.join(".")}.json`); return true; } catch { /* não está publicada */ }
    try { Deno.statSync(`${SAIDA}/${id}.json`); return aprovada(id); } catch { return false; }
  };
  return [...porId.entries()].filter(([id]) => id && !existe(id)).map(([id, itens]) => ({ id, itens: [...new Set(itens)] }))
    .sort((a, b) => b.itens.length - a.itens.length);
}
const falhas = new Map<string, number>();
function aprovada(id: string): boolean {
  const o = new Deno.Command("deno", { args: ["run", "--allow-read", "--allow-env", `${ROOT}scripts/biblioteca/conferir-materia.ts`, `${SAIDA}/${id}.json`], stdout: "piped", stderr: "piped" }).outputSync();
  return new TextDecoder().decode(o.stdout).trim().endsWith("OK");
}

let semAvanco = 0;
log(`fase 2 iniciada: ${pendentes().length} matérias novas a escrever`);
while (true) {
  const fila = pendentes().filter((p) => (falhas.get(p.id) ?? 0) < 3);
  if (!fila.length) { log("todas as matérias novas existem (ou falharam 3 vezes)"); break; }
  const alvo = fila[0];
  log(`rodada: ${alvo.id} (cobre ${alvo.itens.length} itens de edital)`);
  const pedido = `Leia AGENTS.md e a seção "Matéria nova" de conteudo/fila/PEDIDO-CODEX-ITENS-SEM-MATERIA.md.
Escreva AGORA, por inteiro, a matéria de id "${alvo.id}" e grave em conteudo/entrada/materias-codex-2/${alvo.id}.json
(pode escrever em pedaços e juntar). Ela deve cobrir estes itens de edital:
${alvo.itens.slice(0, 25).map((t) => `- ${t}`).join("\n")}
Use conteudo/materias/informatica/flyway-migracao-banco.json como modelo de estrutura. Valide com
deno run --allow-read --allow-env scripts/biblioteca/conferir-materia.ts conteudo/entrada/materias-codex-2/${alvo.id}.json
e corrija até dar OK. Escreva cada capítulo e cada questão você mesmo; não gere conteúdo por script.
Se não souber um número de lei, artigo, prazo ou valor, explique sem o número. Não faça mais nada nesta rodada.`;
  const o = await new Deno.Command("node", {
    args: [CODEX_JS, "exec", "-s", "workspace-write", "--skip-git-repo-check", "-C", ROOT, pedido],
    stdin: "null", stdout: "piped", stderr: "piped",
  }).output();
  const txt = new TextDecoder().decode(o.stdout) + new TextDecoder().decode(o.stderr);
  let ok = false;
  try { Deno.statSync(`${SAIDA}/${alvo.id}.json`); ok = aprovada(alvo.id); } catch { /* não gravou */ }
  if (ok) { semAvanco = 0; log(`   ok: ${alvo.id} gravada e aprovada no conferidor`); await sleep(10_000); continue; }
  falhas.set(alvo.id, (falhas.get(alvo.id) ?? 0) + 1);
  semAvanco++;
  const limite = /usage limit|rate limit|quota|too many requests|429/i.test(txt);
  const espera = limite ? 30 : Math.min(2 * semAvanco, 20);
  log(`   não ficou pronta (${limite ? "limite do plano" : `tentativa ${falhas.get(alvo.id)} de 3`}); esperando ${espera} min`);
  await sleep(espera * 60_000);
}
log("fase 2 concluída");
