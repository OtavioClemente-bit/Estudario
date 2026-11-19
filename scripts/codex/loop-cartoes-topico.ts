// Cartões por tópico do edital (18/11/2026). Muitos tópicos ligam numa matéria maior e cobrem só uma fatia dela
// ("Comissões parlamentares de inquérito" dentro de Poder Legislativo); o app põe primeiro os cartões que falam do
// tópico, mas a matéria quase não tem nenhum. Fila: conteudo/fila/cartoes-topico.json (work/medir-topicos.ts), as
// matérias mais usadas primeiro. O Codex copia a matéria publicada para conteudo/entrada/cartoes-topico/<id>.json e
// escreve À MÃO flashcards e itens de memorização sobre cada tópico listado. Este programa só repete o pedido e confere:
// capítulos e questões idênticos, nada antigo apagado, pelo menos 3 itens por tópico (pelas palavras do tópico),
// acentos sem corrupção, limites (60 flashcards, 30 de memorização) e conferidor OK. Eu reviso e publico.
//   deno run -A scripts/codex/loop-cartoes-topico.ts
const ROOT = new URL("../../", import.meta.url).pathname.replace(/^\/([A-Za-z]:)/, "$1");
const CODEX_JS = `${Deno.env.get("APPDATA")}/npm/node_modules/@openai/codex/bin/codex.js`;
const SAIDA = `${ROOT}conteudo/entrada/cartoes-topico`;
const LOG = `${ROOT}conteudo/entrada/cartoes-topico-log.txt`;
const FALHAS = `${ROOT}conteudo/entrada/cartoes-topico-falhas.json`;
const MAX_TOTAL = 5, MAX_FC = 60, MAX_AR = 30, MIN_POR_TOPICO = 3;
const sleep = (ms: number) => new Promise((r) => setTimeout(r, ms));
const log = (m: string) => { const l = `[${new Date().toLocaleString("pt-BR")}] ${m}`; console.log(l); Deno.writeTextFileSync(LOG, l + "\n", { append: true }); };
// deno-lint-ignore no-explicit-any
type Json = Record<string, any>;
const ler = (p: string) => JSON.parse(Deno.readTextFileSync(p).replace(/^﻿/, ""));
Deno.mkdirSync(SAIDA, { recursive: true });
const publicada = (id: string) => { const [a, ...b] = id.split("."); return `${ROOT}conteudo/materias/${a}/${b.join(".")}.json`; };
const STOP = new Set(["sobre", "entre", "pelos", "pelas", "quando", "outros", "outras", "seus", "suas", "como", "para", "pela", "pelo", "dos", "das", "com", "sem", "nos", "nas", "lei", "artigo", "nocoes", "conceito", "conceitos", "aspectos", "geral", "gerais", "basicos", "principais", "etc", "respectivas", "demais"]);
const stems = (t: string) => [...new Set(String(t ?? "").normalize("NFD").replace(/[̀-ͯ]/g, "").toLowerCase().split(/[^a-z0-9]+/).filter((w) => w.length >= 4 && !STOP.has(w)).map((w) => w.slice(0, 6)))];
const fila = ler(`${ROOT}conteudo/fila/cartoes-topico.json`) as { id: string; topicos: { texto: string }[] }[];
const topicosDe = new Map(fila.map((f) => [f.id, f.topicos.map((t) => t.texto)]));

function faltando(id: string, m: Json): string[] {
  const itens = [...(m.flashcards ?? []), ...(m.activeRecall ?? [])].map((c: Json) => new Set(stems(JSON.stringify(c))));
  return (topicosDe.get(id) ?? []).filter((t) => {
    const s = stems(t); if (s.length === 0) return false;
    return itens.filter((w) => s.filter((x) => w.has(x)).length >= Math.min(2, s.length)).length < MIN_POR_TOPICO;
  });
}

function conferir(id: string): string[] | null {
  let novo: Json, velho: Json;
  try { novo = ler(`${SAIDA}/${id}.json`); } catch (e) { try { Deno.statSync(`${SAIDA}/${id}.json`); return [`JSON inválido: ${e}`]; } catch { return null; } }
  try { velho = ler(publicada(id)); } catch { return ["matéria publicada não encontrada"]; }
  const erros: string[] = [];
  if (JSON.stringify(novo.chapters) !== JSON.stringify(velho.chapters)) erros.push("os capítulos mudaram; recopie-os exatamente da matéria publicada (só flashcards e activeRecall mudam)");
  if (JSON.stringify(novo.questions) !== JSON.stringify(velho.questions)) erros.push("as questões mudaram; recopie-as exatamente da matéria publicada");
  const fc = (novo.flashcards ?? []).length, ar = (novo.activeRecall ?? []).length;
  if (fc > MAX_FC) erros.push(`${fc} flashcards; o máximo é ${MAX_FC}: troque os mais genéricos em vez de só somar`);
  if (ar > MAX_AR) erros.push(`${ar} itens de memorização; o máximo é ${MAX_AR}`);
  if (fc < (velho.flashcards ?? []).length - 8) erros.push("muitos flashcards antigos sumiram; troque só os genéricos ou repetidos");
  const moji = /Ã[\u0080-¿]|â€|Â[\u0080-¿]/g;
  if ((JSON.stringify(novo).match(moji)?.length ?? 0) > (JSON.stringify(velho).match(moji)?.length ?? 0)) erros.push('texto com acento corrompido (ex.: "Ã©" no lugar de "é"); reescreva esses trechos em UTF-8');
  // Repetição (19/11: a amostra achou 15–30% de cartões quase iguais): frente nova parecida demais com outra.
  const velhas = new Set((velho.flashcards ?? []).map((c: Json) => c.front));
  const frentes = (novo.flashcards ?? []).map((c: Json) => ({ f: String(c.front), s: new Set(stems(c.front + " " + c.back)) }));
  const iguais: string[] = [];
  frentes.forEach((a: any, i: number) => { if (velhas.has(a.f)) return; for (const b of frentes.slice(0, i)) { const inter = [...a.s].filter((x) => b.s.has(x)).length; if (inter / Math.max(1, Math.min(a.s.size, b.s.size)) >= 0.8) { iguais.push(`"${a.f}" ≈ "${b.f}"`); break; } } });
  if (iguais.length) erros.push(`cartões quase iguais (troque por cartões sobre outro ponto do tópico): ${iguais.slice(0, 5).join("; ")}`);
  for (const t of faltando(id, novo)) erros.push(`o tópico "${t}" ainda tem menos de ${MIN_POR_TOPICO} cartões/itens que usam as palavras dele`);
  const o = new Deno.Command("deno", { args: ["run", "--allow-read", "--allow-env", `${ROOT}scripts/biblioteca/conferir-materia.ts`, `${SAIDA}/${id}.json`], stdout: "piped", stderr: "piped" }).outputSync();
  const saida = (new TextDecoder().decode(o.stdout) + new TextDecoder().decode(o.stderr)).trim();
  if (!saida.endsWith("OK")) erros.push(`conferidor: ${saida.slice(0, 1500)}`);
  return erros;
}

const total: Record<string, number> = (() => { try { return ler(FALHAS); } catch { return {}; } })();
const contar = (id: string) => { try { Object.assign(total, ler(FALHAS)); } catch { /* primeiro */ } total[id] = (total[id] ?? 0) + 1; Deno.writeTextFileSync(FALHAS, JSON.stringify(total, null, 1)); };
function travar(id: string): boolean {
  const p = `${SAIDA}/${id}.lock`;
  try { if (Date.now() - (Deno.statSync(p).mtime?.getTime() ?? 0) > 3 * 3600_000) Deno.removeSync(p); } catch { /* sem trava */ }
  try { Deno.writeTextFileSync(p, String(Deno.pid), { createNew: true }); return true; } catch { return false; }
}
const soltar = (id: string) => { try { Deno.removeSync(`${SAIDA}/${id}.lock`); } catch { /* já saiu */ } };
const ordem = fila; // sempre os tópicos mais usados primeiro (a conta 2 usa CODEX_ORDEM=reversa para outra fila)
function proxima(): { id: string; erros: string[] | null } | null {
  for (const { id } of ordem) {
    if ((total[id] ?? 0) >= MAX_TOTAL) continue;
    let velho: Json; try { velho = ler(publicada(id)); } catch { continue; }
    const erros = conferir(id);
    if (erros && erros.length === 0) continue;
    if (erros === null && faltando(id, velho).length === 0) continue;
    if (!travar(id)) continue;
    return { id, erros };
  }
  return null;
}

const PARALELO = Number(Deno.env.get("CODEX_PARALELO") ?? 6);
log(`cartões por tópico: ${fila.length} matérias na fila, ${PARALELO} em paralelo`);
async function trabalhador(n: number) {
  await sleep(n * 15_000);
  while (true) {
    const item = proxima();
    if (!item) { log(`[t${n}] nada mais a pegar`); break; }
    const { id, erros } = item;
    const destino = `conteudo/entrada/cartoes-topico/${id}.json`, origem = publicada(id).replace(ROOT, "");
    log(`[t${n}] ${erros ? "correção" : "cartões"}: ${id}`);
    const pedido = erros
      ? `Leia AGENTS.md. O trabalho em ${destino} já começou; NÃO recomece. O conferidor apontou só isto:
${erros.join("\n").slice(0, 3000)}
Corrija AGORA editando o arquivo à mão (apply_patch). Se for o caso, recopie capítulos e questões de ${origem}.
Escreva cada cartão você mesmo, sem script. Não faça outra coisa nesta rodada.`
      : `Leia AGENTS.md. A matéria ${origem} é usada por estes tópicos de edital, mas quase não tem flashcard nem item de
memorização sobre eles (o aluno que estuda o tópico só vê cartões gerais):
${faltando(id, ler(publicada(id))).map((t) => `- ${t}`).join("\n")}
Faça assim:
1. Copie o arquivo inteiro para ${destino} (copiar com o terminal é permitido) e suba "version" em 1.
2. Para CADA tópico acima, escreva VOCÊ MESMO, À MÃO (apply_patch), 3 a 4 flashcards ({"front","back"}) e 1 a 2 itens de
   memorização ({"question","answer"}) sobre o que esse tópico cobra, USANDO AS PALAVRAS DO TÓPICO na frente/pergunta (é por
   elas que o app escolhe os cartões). Conteúdo concreto e correto, tirado da teoria da matéria; nada genérico nem repetido.
3. Máximo de ${MAX_FC} flashcards e ${MAX_AR} itens de memorização: se passar, troque os mais genéricos ou repetidos.
4. NÃO mude capítulos, questões nem nada além de "flashcards", "activeRecall" e "version".
5. Nunca invente número de lei, artigo, prazo, valor ou súmula: use o que está no texto da matéria; na dúvida, sem número.
6. Valide: deno run --allow-read --allow-env scripts/biblioteca/conferir-materia.ts ${destino} e corrija até OK.
Nenhum script gera ou completa texto. Não faça mais nada nesta rodada.`;
    const o = await new Deno.Command("node", {
      args: [CODEX_JS, "exec", "-c", `model_reasoning_effort="${Deno.env.get("CODEX_ESFORCO") ?? "high"}"`, "-s", "workspace-write", "--skip-git-repo-check", "-C", ROOT, pedido],
      stdin: "null", stdout: "piped", stderr: "piped",
    }).output();
    const txt = new TextDecoder().decode(o.stdout) + new TextDecoder().decode(o.stderr);
    const depois = conferir(id);
    if (depois && depois.length === 0) { log(`   [t${n}] ok: ${id} cartões por tópico aprovados`); soltar(id); await sleep(10_000); continue; }
    if (/hit your usage limit|usage limit (reached|has been reached)|try again (in|at) \d|rate_limit_exceeded/i.test(txt.slice(-2_000))) {
      log(`   [t${n}] limite do plano; esperando 30 min`); await sleep(30 * 60_000); soltar(id); continue;
    }
    contar(id);
    log(`   [t${n}] ${id}: ${depois ? `faltam ${depois.length} correções` : "não gravou"} (tentativa ${total[id]} de ${MAX_TOTAL})`);
    soltar(id);
  }
}
await Promise.all([...Array(PARALELO).keys()].map(trabalhador));
log("cartões por tópico: fila concluída");
