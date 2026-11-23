// Conserto à mão, pelo Codex, das matérias escritas pelo ChatGPT que o conferidor reprovou (23/11: 568 paradas,
// quase todas com "a alternativa certa é a mais longa"). Fila: conteudo/fila/consertar-chatgpt-materias.txt
// (um nome de arquivo de conteudo/entrada/chatgpt por linha, sem .json). O Codex edita o próprio arquivo com
// apply_patch; este programa só repete o pedido com a lista do conferidor e confere. Eu reviso e publico depois.
//   deno run -A scripts/codex/loop-consertar-chatgpt.ts
const ROOT = new URL("../../", import.meta.url).pathname.replace(/^\/([A-Za-z]:)/, "$1");
const CODEX_JS = `${Deno.env.get("APPDATA")}/npm/node_modules/@openai/codex/bin/codex.js`;
const PASTA = "conteudo/entrada/chatgpt";
const LOG = `${ROOT}conteudo/entrada/consertar-chatgpt-log.txt`;
const FALHAS = `${ROOT}conteudo/entrada/consertar-chatgpt-falhas.json`;
const TRAVAS = `${ROOT}conteudo/entrada/consertar-chatgpt-travas`;
const MAX_TOTAL = 5;
const sleep = (ms: number) => new Promise((r) => setTimeout(r, ms));
const log = (m: string) => { const l = `[${new Date().toLocaleString("pt-BR")}] ${m}`; console.log(l); Deno.writeTextFileSync(LOG, l + "\n", { append: true }); };
const ler = (p: string) => JSON.parse(Deno.readTextFileSync(p).replace(/^﻿/, ""));
Deno.mkdirSync(TRAVAS, { recursive: true });

function conferir(nome: string): string[] {
  const arq = `${ROOT}${PASTA}/${nome}.json`;
  try { ler(arq); } catch (e) { return [`JSON inválido: ${e}`]; }
  const o = new Deno.Command("deno", { args: ["run", "--allow-read", "--allow-env", `${ROOT}scripts/biblioteca/conferir-materia.ts`, arq], stdout: "piped", stderr: "piped" }).outputSync();
  const s = (new TextDecoder().decode(o.stdout) + new TextDecoder().decode(o.stderr)).trim();
  return s.endsWith("OK") ? [] : [s.slice(0, 2500)];
}
const total: Record<string, number> = (() => { try { return ler(FALHAS); } catch { return {}; } })();
const contar = (id: string) => { try { Object.assign(total, ler(FALHAS)); } catch { /* primeiro */ } total[id] = (total[id] ?? 0) + 1; Deno.writeTextFileSync(FALHAS, JSON.stringify(total, null, 1)); };
const travar = (id: string) => { try { Deno.writeTextFileSync(`${TRAVAS}/${id}.lock`, String(Date.now()), { createNew: true }); return true; } catch { return false; } };
const soltar = (id: string) => { try { Deno.removeSync(`${TRAVAS}/${id}.lock`); } catch { /* já */ } };

const fila = Deno.readTextFileSync(`${ROOT}conteudo/fila/consertar-chatgpt-materias.txt`).split("\n").map((l) => l.trim()).filter(Boolean);
function proxima(): { id: string; erros: string[] } | null {
  for (const id of fila) {
    if ((total[id] ?? 0) >= MAX_TOTAL) continue;
    try { Deno.statSync(`${ROOT}${PASTA}/${id}.json`); } catch { continue; }
    const erros = conferir(id);
    if (!erros.length) continue;
    if (!travar(id)) continue;
    return { id, erros };
  }
  return null;
}

const PARALELO = Number(Deno.env.get("CODEX_PARALELO") ?? 6);
log(`consertar ChatGPT iniciado: ${fila.length} matérias na lista, ${PARALELO} em paralelo`);
async function trabalhador(n: number) {
  await sleep(n * 15_000);
  while (true) {
    const item = proxima();
    if (!item) { log(`[c${n}] nada mais a pegar`); break; }
    const { id, erros } = item;
    const arq = `${PASTA}/${id}.json`;
    log(`[c${n}] consertar: ${id}`);
    const pedido = `Leia AGENTS.md. A matéria ${arq} foi reprovada pelo conferidor. Ele apontou:
${erros.join("\n")}
Corrija À MÃO, editando o próprio arquivo com apply_patch, só o que foi apontado:
- "a alternativa certa é a mais longa": em cada questão em que a certa é a mais longa, encurte a certa (sem perder a exatidão) e/ou alongue as erradas com conteúdo plausível do mesmo tema, até a certa ser a mais longa em no máximo 40% das questões. O gabarito continua certo; se trocar a posição da certa, atualize o gabarito.
- frase repetida nas explicações: reescreva cada explicação apontada para dizer por que AQUELA resposta está certa.
- data de fonte "AAAA-MM-DD": ponha a data de hoje no formato pedido.
- outros apontamentos: corrija o que o conferidor diz.
Nenhum script gera, completa, embaralha ou "equilibra" texto: cada frase é escrita por você. Não mude capítulos.
Nunca invente número de lei, artigo, prazo ou súmula. Valide com: deno run --allow-read --allow-env scripts/biblioteca/conferir-materia.ts ${arq} e corrija até OK. Não faça mais nada nesta rodada.`;
    const o = await new Deno.Command("node", {
      args: [CODEX_JS, "exec", "-c", `model_reasoning_effort="${Deno.env.get("CODEX_ESFORCO") ?? "high"}"`, "-s", "workspace-write", "--skip-git-repo-check", "-C", ROOT, pedido],
      stdin: "null", stdout: "piped", stderr: "piped",
    }).output();
    const txt = new TextDecoder().decode(o.stdout) + new TextDecoder().decode(o.stderr);
    const depois = conferir(id);
    if (!depois.length) { log(`   [c${n}] ok: ${id} aprovada no conferidor`); soltar(id); await sleep(10_000); continue; }
    if (/hit your usage limit|usage limit (reached|has been reached)|try again (in|at) \d|rate_limit_exceeded/i.test(txt.slice(-2_000))) {
      log(`   [c${n}] limite do plano; esperando 30 min`); await sleep(30 * 60_000); soltar(id); continue;
    }
    contar(id);
    log(`   [c${n}] ${id}: ainda reprovada (tentativa ${total[id]} de ${MAX_TOTAL})`);
    soltar(id);
  }
}
await Promise.all([...Array(PARALELO).keys()].map(trabalhador));
log("consertar ChatGPT: fila concluída");
