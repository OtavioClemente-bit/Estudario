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
    let j: Json;
    try { j = ler(`${ITENS}/${e.name}`); } catch { continue; } // decisão com JSON quebrado: não derruba o loop
    if (j.decisao === "materia-nova" || j.decisao === "ligar") {
      if (j.decisao === "materia-nova" || porId.has(j.id)) porId.set(j.id, [...(porId.get(j.id) ?? []), String(j.texto)]);
    }
  }
  const existe = (id: string) => {
    const [a, ...b] = id.split(".");
    try { Deno.statSync(`${ROOT}conteudo/materias/${a}/${b.join(".")}.json`); return true; } catch { /* não está publicada */ }
    try { Deno.statSync(`${SAIDA}/${id}.json`); return aprovada(id); } catch { return false; }
  };
  // Matérias pedidas em outro lugar (chat do ChatGPT pelo navegador): o Codex não pega.
  let reservadas = new Set<string>();
  try { reservadas = new Set(Deno.readTextFileSync(`${ROOT}conteudo/entrada/materias-reservadas.txt`).split(/\s+/).filter(Boolean)); } catch { /* sem reservas */ }
  return [...porId.entries()].filter(([id]) => id && !existe(id) && !reservadas.has(id)).map(([id, itens]) => ({ id, itens: [...new Set(itens)] }))
    .sort((a, b) => b.itens.length - a.itens.length);
}
// Tentativas de cada matéria, guardadas entre execuções: o ciclo do .cmd reabre este programa e não deve insistir
// para sempre na mesma. Até 12 rodadas no total (escrita + correções); depois o rascunho fica para a minha revisão.
const FALHAS = `${ROOT}conteudo/entrada/materias-codex-2-falhas.json`;
const MAX_TOTAL = 12;
const total: Record<string, number> = (() => { try { return ler(FALHAS); } catch { return {}; } })();
const falhas = new Map<string, number>();
const contarFalha = (id: string) => {
  falhas.set(id, (falhas.get(id) ?? 0) + 1);
  total[id] = (total[id] ?? 0) + 1;
  Deno.writeTextFileSync(FALHAS, JSON.stringify(total, null, 1));
};
function aprovada(id: string): boolean {
  const o = new Deno.Command("deno", { args: ["run", "--allow-read", "--allow-env", `${ROOT}scripts/biblioteca/conferir-materia.ts`, `${SAIDA}/${id}.json`], stdout: "piped", stderr: "piped" }).outputSync();
  return new TextDecoder().decode(o.stdout).trim().endsWith("OK");
}

// Várias rodadas do Codex ao mesmo tempo, cada uma numa matéria diferente (CODEX_PARALELO, padrão 6).
const PARALELO = Number(Deno.env.get("CODEX_PARALELO") ?? 6);
const emAndamento = new Set<string>();
log(`fase 2 iniciada: ${pendentes().length} matérias novas a escrever, ${PARALELO} em paralelo`);
async function trabalhador(n: number) {
 let semAvanco = 0;
 let fixo: { id: string; itens: string[] } | null = null;
 await sleep(n * 15_000);
 while (true) {
  const fila = pendentes().filter((p) => (falhas.get(p.id) ?? 0) < 6 && (total[p.id] ?? 0) < MAX_TOTAL && !emAndamento.has(p.id));
  if (!fixo && !fila.length) { log(`[${n}] nada mais a pegar`); break; }
  const alvo: { id: string; itens: string[] } = fixo ?? fila[0];
  fixo = null;
  emAndamento.add(alvo.id);
  // O Codex escreve a matéria inteira numa rodada, mas encerra antes de corrigir. Por isso a matéria fica com o
  // mesmo trabalhador: as rodadas seguintes mandam só a lista do conferidor e pedem para corrigir aquilo.
  const conferir = () => {
    try { Deno.statSync(`${SAIDA}/${alvo.id}.json`); } catch { return null; }
    const o = new Deno.Command("deno", { args: ["run", "--allow-read", "--allow-env", `${ROOT}scripts/biblioteca/conferir-materia.ts`, `${SAIDA}/${alvo.id}.json`], stdout: "piped", stderr: "piped" }).outputSync();
    return (new TextDecoder().decode(o.stdout) + new TextDecoder().decode(o.stderr)).trim();
  };
  const erros = conferir();
  log(`[${n}] rodada: ${alvo.id} (cobre ${alvo.itens.length} itens de edital)`);
  const pedido = erros ? `Leia AGENTS.md. A matéria conteudo/entrada/materias-codex-2/${alvo.id}.json já está escrita; NÃO a reescreva.
O conferidor apontou só isto:
${erros.slice(0, 3000)}
Corrija AGORA esses pontos editando o arquivo (alongue explicações curtas para 120+ caracteres explicando o porquê;
equilibre o tamanho das alternativas encurtando a certa ou alongando as erradas; troque a letra da certa entre A–E
onde o gabarito estiver concentrado; complete questões se faltar). Escreva cada texto você mesmo, sem script.
PROIBIDO: ao equilibrar, NUNCA escreva uma alternativa errada que diga o mesmo que a certa com outras palavras
(isso cria duas corretas e a matéria é rejeitada na revisão). Cada alternativa errada precisa estar ERRADA de verdade.
IMPORTANTE: o AGENTS.md proíbe só PROGRAMA que gere/embaralhe/equilibre. Reescrever você mesmo, à mão, o texto de
cada explicação e de cada alternativa (editando o arquivo com apply_patch, questão por questão; se o JSON estiver numa
linha só, rode antes deno fmt no arquivo) é exatamente o
permitido e é o que está sendo pedido. Rodar o conferidor no terminal também é permitido. Não recuse por isso.
Se for muita coisa, corrija o máximo que conseguir nesta rodada; a próxima rodada continua de onde parou.
Rode o conferidor de novo e repita até dar OK. Não pare antes do OK e não faça outra coisa.` : `Leia AGENTS.md e a seção "Matéria nova" de conteudo/fila/PEDIDO-CODEX-ITENS-SEM-MATERIA.md.
Escreva AGORA, por inteiro, a matéria de id "${alvo.id}" e grave em conteudo/entrada/materias-codex-2/${alvo.id}.json
(pode escrever em pedaços e juntar). Ela deve cobrir estes itens de edital:
${alvo.itens.slice(0, 25).map((t) => `- ${t}`).join("\n")}
Use conteudo/materias/informatica/flyway-migracao-banco.json como modelo de estrutura. Valide com
deno run --allow-read --allow-env scripts/biblioteca/conferir-materia.ts conteudo/entrada/materias-codex-2/${alvo.id}.json
e corrija até dar OK. Escreva cada capítulo e cada questão você mesmo; não gere conteúdo por script.
Se não souber um número de lei, artigo, prazo ou valor, explique sem o número. Não faça mais nada nesta rodada.
Ensine fatos CONCRETOS do assunto (para norma: órgãos, competências, composição, requisitos, prazos e procedimentos
que estão no texto oficial; para município/povo/região: dados reais de história, geografia e cultura). Abra o texto
oficial na internet antes de escrever. Matéria só com generalidades ("é importante consultar a norma", "o servidor
deve agir com ética") é rejeitada na revisão. Confira em cada questão: só UMA alternativa defensável, gabarito
marcado na alternativa certa e explicação da própria questão.
Se o arquivo já existir, NÃO recomece: rode o conferidor e corrija só o que ele apontar (explicações curtas,
alternativa certa longa demais, gabarito concentrado numa letra) até dar OK.
Se os itens forem só números de atos sem assunto e você não achar o conteúdo, escreva a matéria sobre o tipo de ato
(o que é, quem edita, para que serve, tramitação) sem inventar o conteúdo de cada número.`;
  const o = await new Deno.Command("node", {
    // Esforço médio só neste loop (no leve ele desistia no meio das correções); o Codex do dia a dia segue no leve.
    args: [CODEX_JS, "exec", "-c", `model_reasoning_effort="${Deno.env.get("CODEX_ESFORCO") ?? "medium"}"`, "-s", "workspace-write", "--skip-git-repo-check", "-C", ROOT, pedido],
    stdin: "null", stdout: "piped", stderr: "piped",
  }).output();
  const txt = new TextDecoder().decode(o.stdout) + new TextDecoder().decode(o.stderr);
  Deno.writeTextFileSync(`${ROOT}conteudo/entrada/materias-codex-2-ultima-saida-${n}.txt`, txt.slice(0, 3000) + "\n...\n" + txt.slice(-8000));
  let ok = false;
  try { Deno.statSync(`${SAIDA}/${alvo.id}.json`); ok = aprovada(alvo.id); } catch { /* não gravou */ }
  emAndamento.delete(alvo.id);
  // Limite do plano só pela mensagem do próprio Codex, no fim da saída. Não conta como tentativa: espera e repete.
  if (!ok && /hit your usage limit|usage limit (reached|has been reached)|try again (in|at) \d|rate_limit_exceeded/i.test(txt.slice(-2_000))) {
    log(`   [${n}] limite do plano; esperando 30 min`);
    // Segura a matéria durante a espera, para os outros trabalhadores não pegarem a mesma e escreverem em dobro.
    emAndamento.add(alvo.id);
    await sleep(30 * 60_000);
    emAndamento.delete(alvo.id);
    continue;
  }
  // Rodadas de correção (CODEX_CORRECOES, padrão 5): a regra 1 do AGENTS.md agora diz que corrigir à mão é permitido
  // (antes ele recusava). Depois das correções, o rascunho reprovado fica para a minha revisão.
  if (!ok && conferir() !== null && (falhas.get(alvo.id) ?? 0) < Number(Deno.env.get("CODEX_CORRECOES") ?? 5)) { contarFalha(alvo.id); fixo = alvo; emAndamento.add(alvo.id); log(`   [${n}] ${alvo.id}: gravada, faltam correções; mandando a lista do conferidor`); continue; }
  if (ok) { semAvanco = 0; log(`   [${n}] ok: ${alvo.id} gravada e aprovada no conferidor`); await sleep(10_000); continue; }
  contarFalha(alvo.id);
  semAvanco++;
  log(`   [${n}] ${alvo.id} não ficou pronta (rascunho fica para revisão)`);
 }
}
await Promise.all([...Array(PARALELO).keys()].map(trabalhador));
log("fase 2 concluída");
