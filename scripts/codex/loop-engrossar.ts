// Engrossar recortes magros pelo Codex (14/11/2026). Recorte magro = tópico do edital que abre só alguns capítulos
// de uma matéria e, com esse filtro, fica com menos de 4 flashcards ou de 2 itens de memorização (mesma conta do
// scripts/biblioteca/recortes-magros.ts, que usa a montagem do servidor). O Codex copia a matéria publicada para
// conteudo/entrada/engrossar-codex/<id>.json e escreve À MÃO flashcards e memorização sobre esses capítulos. Este
// programa só repete o pedido e confere: capítulos e questões idênticos, nada antigo apagado sem troca, conferidor OK
// e nenhum recorte da matéria magro. Eu reviso e publico depois. Várias contas podem rodar juntas (trava por arquivo).
//   deno run -A scripts/codex/loop-engrossar.ts
import { assembleFromLibrary } from "../../supabase/functions/_shared/library.ts";

const ROOT = new URL("../../", import.meta.url).pathname.replace(/^\/([A-Za-z]:)/, "$1");
const CODEX_JS = `${Deno.env.get("APPDATA")}/npm/node_modules/@openai/codex/bin/codex.js`;
const SAIDA = `${ROOT}conteudo/entrada/engrossar-codex`;
const LOG = `${ROOT}conteudo/entrada/engrossar-codex-log.txt`;
const FALHAS = `${ROOT}conteudo/entrada/engrossar-codex-falhas.json`;
const MAX_TOTAL = 6;
const MIN_FLASHCARDS = 4, MIN_RECALL = 2, MAX_FLASHCARDS = 30;
const sleep = (ms: number) => new Promise((r) => setTimeout(r, ms));
const log = (m: string) => {
  const l = `[${new Date().toLocaleString("pt-BR")}] ${m}`;
  console.log(l);
  Deno.writeTextFileSync(LOG, l + "\n", { append: true });
};
// deno-lint-ignore no-explicit-any
type Json = Record<string, any>;
const ler = (p: string) => JSON.parse(Deno.readTextFileSync(p).replace(/^﻿/, ""));
Deno.mkdirSync(SAIDA, { recursive: true });
const publicada = (id: string) => { const [a, ...b] = id.split("."); return `${ROOT}conteudo/materias/${a}/${b.join(".")}.json`; };

// Recortes (conjuntos de capítulos) de cada matéria, tirados de conteudo/foco.
const recortes = new Map<string, Array<{ capitulos: number[]; questoes: string[]; texto: string }>>();
for (const f of Deno.readDirSync(`${ROOT}conteudo/foco`)) {
  if (!f.name.endsWith(".json")) continue;
  const foco = ler(`${ROOT}conteudo/foco/${f.name}`);
  const lista = recortes.get(String(foco.materia)) ?? [];
  const vistos = new Set(lista.map((r) => r.capitulos.join(",")));
  for (const t of foco.topicos ?? []) {
    const caps = Array.isArray(t.capitulos) ? [...t.capitulos].filter(Number.isInteger).sort((a: number, b: number) => a - b) : [];
    if (caps.length === 0 || vistos.has(caps.join(","))) continue;
    vistos.add(caps.join(","));
    lista.push({ capitulos: caps, questoes: t.questoes ?? [], texto: String(t.texto ?? "").slice(0, 200) });
  }
  recortes.set(String(foco.materia), lista);
}
const input = {
  board: null, avoidStatements: [],
  options: { blocks: ["THEORY", "SUMMARY", "QUICK_REVIEW", "TIPS_TRAPS", "ACTIVE_RECALL", "QUESTIONS", "ERROR_CONCEPTS"], depth: "BOOK", questionCount: 10, questionStyle: "MIXED", difficulty: "MIXED" },
};
/** Recortes magros da matéria: "capítulos X / Y: N cartões, M memorização". */
function magros(id: string, m: Json): string[] {
  const caps = (m.chapters as Json[]) ?? [];
  const out: string[] = [];
  for (const r of recortes.get(id) ?? []) {
    const c = r.capitulos.filter((i) => i < caps.length);
    if (c.length === 0 || c.length >= caps.length) continue;
    let content: Json | null = null;
    try { content = assembleFromLibrary(m as never, null, input as never, `recorte:${id}|${c}`, { capitulos: c, questoes: r.questoes }, true) as Json; } catch { /* vazio */ }
    const fc = content?.flashcards?.length ?? 0, ar = content?.activeRecall?.length ?? 0;
    if (fc < MIN_FLASHCARDS || ar < MIN_RECALL) out.push(`capítulo(s) ${c.map((i) => `"${caps[i]?.title}"`).join(" + ")}: ${fc} flashcards e ${ar} itens de memorização (precisa de ${MIN_FLASHCARDS}+ e ${MIN_RECALL}+)`);
  }
  return out;
}

// Problemas (vazio = aprovada; null = ainda não começou).
function conferir(id: string): string[] | null {
  let novo: Json, velho: Json;
  try { novo = ler(`${SAIDA}/${id}.json`); } catch (e) { try { Deno.statSync(`${SAIDA}/${id}.json`); return [`JSON inválido: ${e}`]; } catch { return null; } }
  try { velho = ler(publicada(id)); } catch { return ["matéria publicada não encontrada"]; }
  const erros: string[] = [];
  if (JSON.stringify(novo.chapters) !== JSON.stringify(velho.chapters)) erros.push("os capítulos mudaram; copie-os de volta exatamente como estão na matéria publicada (só flashcards e activeRecall mudam)");
  if (JSON.stringify(novo.questions) !== JSON.stringify(velho.questions)) erros.push("as questões mudaram; copie-as de volta exatamente como estão na matéria publicada");
  const fc = (novo.flashcards ?? []).length, ar = (novo.activeRecall ?? []).length;
  if (fc > MAX_FLASHCARDS) erros.push(`${fc} flashcards; o máximo é ${MAX_FLASHCARDS}: troque os mais genéricos em vez de só somar`);
  if (fc < (velho.flashcards ?? []).length - 6) erros.push("muitos flashcards antigos sumiram; troque só os genéricos ou repetidos");
  if (ar < (velho.activeRecall ?? []).length) erros.push("itens de memorização antigos sumiram; mantenha-os e acrescente");
  // Acento corrompido (14/11: 3 vieram com "Ã©" no lugar de "é").
  const moji = /Ã[-¿]|â€|Â[-¿]/g;
  if ((JSON.stringify(novo).match(moji)?.length ?? 0) > (JSON.stringify(velho).match(moji)?.length ?? 0)) erros.push('texto com acento corrompido (ex.: "Ã©" no lugar de "é", "Ã§" no lugar de "ç"); reescreva esses trechos com os acentos certos, em UTF-8');
  erros.push(...magros(id, novo));
  const o = new Deno.Command("deno", { args: ["run", "--allow-read", "--allow-env", `${ROOT}scripts/biblioteca/conferir-materia.ts`, `${SAIDA}/${id}.json`], stdout: "piped", stderr: "piped" }).outputSync();
  const saida = (new TextDecoder().decode(o.stdout) + new TextDecoder().decode(o.stderr)).trim();
  if (!saida.endsWith("OK")) erros.push(`conferidor: ${saida.slice(0, 1500)}`);
  return erros;
}

const total: Record<string, number> = (() => { try { return ler(FALHAS); } catch { return {}; } })();
const contar = (id: string) => {
  try { Object.assign(total, ler(FALHAS)); } catch { /* primeiro */ }
  total[id] = (total[id] ?? 0) + 1;
  Deno.writeTextFileSync(FALHAS, JSON.stringify(total, null, 1));
};
// Fila: matérias publicadas com recorte magro, na ordem de work/recortes-magros.json (mais tópicos primeiro).
const fila = [...new Set((ler(`${ROOT}work/recortes-magros.json`) as Json[]).map((s) => String(s.topic_id)))];
const ordem = Deno.env.get("CODEX_ORDEM") === "reversa" ? [...fila].reverse() : fila;
// Trava entre contas: <id>.lock criado de forma exclusiva; trava com mais de 3 h é de quem morreu.
function travar(id: string): boolean {
  const p = `${SAIDA}/${id}.lock`;
  try { if (Date.now() - (Deno.statSync(p).mtime?.getTime() ?? 0) > 3 * 3600_000) Deno.removeSync(p); } catch { /* sem trava */ }
  try { Deno.writeTextFileSync(p, String(Deno.pid), { createNew: true }); return true; } catch { return false; }
}
const soltar = (id: string) => { try { Deno.removeSync(`${SAIDA}/${id}.lock`); } catch { /* já saiu */ } };
function proxima(): { id: string; erros: string[] | null } | null {
  for (const id of ordem) {
    if ((total[id] ?? 0) >= MAX_TOTAL) continue;
    let velho: Json;
    try { velho = ler(publicada(id)); } catch { continue; }
    const erros = conferir(id);
    if (erros && erros.length === 0) continue;
    if (erros === null && magros(id, velho).length === 0) continue; // já engrossada na publicada
    if (!travar(id)) continue;
    return { id, erros };
  }
  return null;
}

const PARALELO = Number(Deno.env.get("CODEX_PARALELO") ?? 6);
log(`engrossar iniciado: ${fila.length} matérias na lista, ${PARALELO} em paralelo`);
async function trabalhador(n: number) {
  await sleep(n * 15_000);
  while (true) {
    const item = proxima();
    if (!item) { log(`[e${n}] nada mais a pegar`); break; }
    const { id, erros } = item;
    const destino = `conteudo/entrada/engrossar-codex/${id}.json`;
    const origem = publicada(id).replace(ROOT, "");
    log(`[e${n}] ${erros ? "correção" : "engrossar"}: ${id}`);
    const pedido = erros
      ? `Leia AGENTS.md. O engrossamento de ${destino} já começou; NÃO recomece.
O conferidor apontou só isto:
${erros.join("\n").slice(0, 3000)}
Corrija AGORA esses pontos editando o arquivo à mão (apply_patch). Se for o caso, recopie capítulos e questões de ${origem}.
Escreva cada flashcard e cada item de memorização você mesmo, sem script. Não faça outra coisa nesta rodada.`
      : `Leia AGENTS.md. A matéria ${origem} tem recortes do edital que abrem só alguns capítulos e, nesses recortes, quase não
sobram flashcards nem itens de memorização ativa:
${magros(id, ler(publicada(id))).join("\n").slice(0, 3000)}
Faça assim:
1. Copie o arquivo inteiro para ${destino} (copiar com o terminal é permitido) e suba "version" em 1.
2. Leia o markdown desses capítulos e escreva VOCÊ MESMO, À MÃO (apply_patch), para CADA um deles, pelo menos 5 flashcards
   ({"front","back"}) e 3 itens de memorização ({"question","answer"}) sobre o que ESSE capítulo ensina, usando os termos
   dele (é por esses termos que o app escolhe os cartões do recorte). Frente curta e objetiva; verso com a resposta e o
   porquê. Nada genérico, nada repetido.
3. A matéria tem no máximo ${MAX_FLASHCARDS} flashcards: se passar, troque os mais genéricos ou repetidos dos outros
   capítulos. Itens de memorização antigos ficam todos.
4. NÃO mude capítulos, questões nem nada além de "flashcards", "activeRecall" e "version".
5. Nunca invente número de lei, artigo, prazo, valor ou súmula: use só o que está no texto da matéria; na dúvida, sem número.
6. Valide com: deno run --allow-read --allow-env scripts/biblioteca/conferir-materia.ts ${destino} e corrija até OK.
Nenhum script gera, completa ou reescreve texto. Não faça mais nada nesta rodada.`;
    const o = await new Deno.Command("node", {
      args: [CODEX_JS, "exec", "-c", `model_reasoning_effort="${Deno.env.get("CODEX_ESFORCO") ?? "high"}"`, "-s", "workspace-write", "--skip-git-repo-check", "-C", ROOT, pedido],
      stdin: "null", stdout: "piped", stderr: "piped",
    }).output();
    const txt = new TextDecoder().decode(o.stdout) + new TextDecoder().decode(o.stderr);
    const depois = conferir(id);
    if (depois && depois.length === 0) { log(`   [e${n}] ok: ${id} engrossada e aprovada`); soltar(id); await sleep(10_000); continue; }
    if (/hit your usage limit|usage limit (reached|has been reached)|try again (in|at) \d|rate_limit_exceeded/i.test(txt.slice(-2_000))) {
      log(`   [e${n}] limite do plano; esperando 30 min`);
      await sleep(30 * 60_000); // segura a matéria durante a espera
      soltar(id);
      continue;
    }
    contar(id);
    log(`   [e${n}] ${id}: ${depois ? `faltam ${depois.length} correções` : "não gravou"} (tentativa ${total[id]} de ${MAX_TOTAL})`);
    soltar(id);
  }
}
await Promise.all([...Array(PARALELO).keys()].map(trabalhador));
log("engrossar: fila concluída");
