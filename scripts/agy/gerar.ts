// Gerador contínuo de questões pelo Antigravity CLI (exceção 3 do AGENTS.md).
// Escolhe o recorte mais usado nos editais que ainda tem poucas questões, pede um lote a um modelo
// (Gemini 4 Argon), confere, manda outro modelo (Claude Opus) auditar, descarta o que for apontado,
// junta o resto ao recorte e, de tempos em tempos, publica e grava no git. Quando a cota acaba, espera.
//   deno run -A scripts/agy/gerar.ts            (roda sem parar)
//   deno run -A scripts/agy/gerar.ts --uma-vez  (só um lote, para teste)
//   deno run -A scripts/agy/gerar.ts --fila     (só mostra a fila)
// O script nunca escreve nem altera questão: só envia o pedido, confere, descarta e junta.
import { normalizeBoard, validateBoardNote } from "../../supabase/functions/_shared/library.ts";

const ROOT = new URL("../../", import.meta.url).pathname.replace(/^\/([A-Za-z]:)/, "$1");
const AGY = "C:/Users/otavi/AppData/Local/agy/bin/agy.exe";
const BASH = "C:/Program Files/Git/bin/bash.exe";
const GEN_MODEL = "gemini-4-argon-medium";
const AUD_MODEL = "claude-opus-5-5-medium";
const AUD_GEMINI = "gemini-4-argon-high";
const META = 150;            // um recorte com isso já está bom; passa para o próximo
const LOTE = 50;
const MIN_EDITAIS = 15;
const PUBLICAR_A_CADA = 5;   // lotes aceitos entre uma publicação e outra
const WORK = `${ROOT}conteudo/entrada/agy`;
const args = new Set(Deno.args);

type Json = Record<string, any>;
const sleep = (ms: number) => new Promise((r) => setTimeout(r, ms));
const norm = (s: string) => String(s ?? "").toLowerCase().normalize("NFD").replace(/[\u0300-\u036f]/g, "").replace(/[^a-z0-9]+/g, " ").trim();
Deno.mkdirSync(`${WORK}/_trabalho`, { recursive: true });
Deno.mkdirSync(`${WORK}/aceitos`, { recursive: true });
function log(msg: string) {
  const line = `[${new Date().toLocaleString("pt-BR")}] ${msg}`;
  console.log(line);
  Deno.writeTextFileSync(`${WORK}/log.txt`, line + "\n", { append: true });
}
const estadoPath = `${WORK}/estado.json`;
let estado: Json = { pares: {}, aceitosDesdePublicacao: 0, totalAceitas: 0 };
try { estado = JSON.parse(Deno.readTextFileSync(estadoPath)); } catch { /* primeira vez */ }
const salvarEstado = () => Deno.writeTextFileSync(estadoPath, JSON.stringify(estado, null, 1));

// ------------------------------------------------------------------ fila
function walk(dir: string, out: string[] = []) {
  for (const e of Deno.readDirSync(dir)) {
    if (e.isDirectory) walk(`${dir}/${e.name}`, out);
    else if (e.name.endsWith(".json")) out.push(`${dir}/${e.name}`);
  }
  return out;
}
function montarFila() {
  const recortes = new Map<string, { path: string; n: number; ce: boolean; board: string }>();
  for (const p of walk(`${ROOT}conteudo/recortes`)) {
    const r = JSON.parse(Deno.readTextFileSync(p));
    const qs = (r.questions ?? []) as Json[];
    // Cebraspe e Quadrix cobram Certo/Errado; nas outras, vale o formato que já predomina no recorte.
    const ce = ["cebraspe", "instituto quadrix"].includes(normalizeBoard(r.board)) || qs.filter((q) => q.format === "TRUE_FALSE").length > qs.length / 2;
    // Atualidades dependem de fatos recentes que o modelo pode não conhecer: ficam de fora.
    if (String(r.topic).startsWith("atualidades.")) continue;
    recortes.set(`${r.topic}|${normalizeBoard(r.board)}`, { path: p, n: qs.length, ce, board: r.board });
  }
  const uso = new Map<string, number>();
  for (const e of Deno.readDirSync(`${ROOT}conteudo/editais`)) {
    if (!e.name.endsWith(".json")) continue;
    const ed = JSON.parse(Deno.readTextFileSync(`${ROOT}conteudo/editais/${e.name}`));
    const b = normalizeBoard(String(ed.banca ?? ""));
    if (!b) continue;
    const ids = new Set<string>();
    for (const d of ed.disciplinas ?? []) for (const t of d.topicos ?? []) if (t.topico) ids.add(t.topico);
    for (const id of ids) uso.set(`${id}|${b}`, (uso.get(`${id}|${b}`) ?? 0) + 1);
  }
  const agora = Date.now();
  return [...recortes.entries()]
    .map(([key, r]) => ({ key, ...r, uso: uso.get(key) ?? 0, est: estado.pares[key] ?? {} }))
    .filter((x) => x.uso >= MIN_EDITAIS && x.n < META)
    // par que falhou 3 vezes seguidas descansa um dia
    .filter((x) => !((x.est.falhasSeguidas ?? 0) >= 3 && agora - (x.est.ultimo ?? 0) < 86_400_000))
    .sort((a, b) => b.uso * (META - b.n) - a.uso * (META - a.n));
}

// ------------------------------------------------------------------ pedido
function montarPedido(item: Json, recorte: Json, materia: Json, retorno = "") {
  const caps = (materia.chapters as Json[]).map((c) => String(c.title));
  const porCap = caps.map((c, i) => `- "${c}" — ${Math.floor(LOTE / caps.length) + (i < LOTE % caps.length ? 1 : 0)}`).join("\n");
  const estilo = String(recorte.questions?.[0]?.estilo ?? recorte.board);
  const topico = String(recorte.questions?.[0]?.topico ?? materia.title);
  const evitar = (recorte.questions as Json[]).slice(-40).map((q) => `- ${String(q.statement).replace(/\s+/g, " ").slice(0, 110)}`).join("\n");
  const formato = item.ce
    ? `"format": "TRUE_FALSE",
  "options": [ { "key": "C", "text": "Certo", "correct": true|false }, { "key": "E", "text": "Errado", "correct": true|false } ],`
    : `"format": "MULTIPLE_CHOICE",
  "options": [ { "key": "A", "text": "...", "correct": false, "comentario": "erro exato desta alternativa (até 140 caracteres)" }, ...B, C, D, E ],`;
  const regrasFormato = item.ce
    ? `- Exatamente ${LOTE / 2} itens Certo e ${LOTE / 2} Errado. O item Errado troca UM detalhe plausível (um termo, um prazo, um órgão, "obrigatoriamente" onde é facultativo), nunca algo obviamente falso.
- Pelo menos 30 itens partem de um TRECHO de 2 a 5 linhas escrito por você (notícia, relatório, despacho, caso concreto), cada um sobre um assunto diferente, e fazem uma afirmação sobre ele.`
    : `- Exatamente 5 alternativas e UMA correta. As cinco no MESMO formato e tamanho parecido: se a certa é só um valor, as erradas também; a justificativa vai no "comentario". A correta pode ser a mais longa em no máximo 12 e a mais curta em no máximo 12. Não "equilibre" letras (o app embaralha).
- Pelo menos 25 questões partem de um caso ou trecho de 2 a 5 linhas escrito por você, cada um sobre assunto diferente.
- Alternativas erradas são erros que candidatos de verdade cometem; proibido alternativa absurda.`;
  return `Você é professor de um cursinho caro e escreve questões para um app de concursos. Escreva ${LOTE} questões
INÉDITAS de "${materia.title}" no estilo da banca ${recorte.board}.

ENTREGA: responda SÓ com um bloco de código \`\`\`json contendo um array de ${LOTE} objetos, nada fora do bloco.
Não use ferramentas nem crie arquivos; apenas escreva a resposta.

Formato de cada objeto (chaves exatamente assim):
{
  "topico": "${topico}",
  "estilo": "${estilo}",
  "statement": "enunciado completo",
  ${formato}
  "difficulty": "FACIL" | "MEDIA" | "DIFICIL",
  "explanation": "150 a 350 caracteres: a regra, a aplicação ao caso e o erro típico; sem citar letra de alternativa",
  "section": "título exato de um capítulo abaixo",
  "errorConceptKey": null,
  "sourceType": "AUTHORIAL", "sourceUrl": null, "board": null, "agency": null, "year": null
}

CAPÍTULOS ("section" igual, letra por letra) e quantas em cada:
${porCap}
Dificuldade: ${Math.round(LOTE * 0.3)} FACIL, ${LOTE - 2 * Math.round(LOTE * 0.3)} MEDIA, ${Math.round(LOTE * 0.3)} DIFICIL.

REGRAS:
${regrasFormato}
- Cada assunto ou regra é foco de no máximo 2 questões. Nenhum enunciado igual a outro (varie a redação do comando).
- Nunca escreva "conforme a matéria", "segundo a teoria" ou "o texto estudado": cada questão se sustenta sozinha.
- Gabarito sem ambiguidade: não faça a resposta depender de ponto em que a norma, a lei ou os gramáticos admitem dois
  entendimentos, a não ser que o enunciado trate exatamente disso.
- Se não tiver certeza de um número de lei, artigo, prazo, valor ou súmula, não faça a questão depender dele. Nunca invente norma.
- O enunciado não pode entregar a resposta.

NÃO REPITA estas questões, que já existem:
${evitar}
${retorno ? `\nNA TENTATIVA ANTERIOR, isto falhou; corrija:\n${retorno}\n` : ""}
ANTES DE RESPONDER, CONFIRA: ${LOTE} questões; distribuição; nenhuma repetida; cada gabarito conferido duas vezes.`;
}

// ------------------------------------------------------------------ chamada ao agy
class Cota extends Error {}
async function agy(model: string, prompt: string, minutos: number): Promise<string> {
  const pedido = `${WORK}/_trabalho/pedido.txt`;
  Deno.writeTextFileSync(pedido, prompt);
  // O pedido vai num arquivo (a linha de comando do Windows tem limite de tamanho).
  const cmd = new Deno.Command(AGY, {
    args: ["-p", "Leia o arquivo pedido.txt desta pasta e cumpra exatamente o que ele pede. Responda só o que ele pede.",
      "--model", model, "--output-format", "json", "--print-timeout", `${minutos}m`, "--disable-slash-commands"],
    cwd: `${WORK}/_trabalho`, stdout: "piped", stderr: "piped",
  });
  const out = await cmd.output();
  const stdout = new TextDecoder().decode(out.stdout), stderr = new TextDecoder().decode(out.stderr);
  let j: Json | null = null;
  try { j = JSON.parse(stdout); } catch { /* sem json */ }
  const texto = `${stderr} ${j?.error ?? ""} ${j?.status ?? ""}`;
  if (/quota|rate.?limit|exhaust|resource.?exhausted|429|limit reached|usage limit/i.test(texto)) throw new Cota(texto.slice(0, 300));
  if (!j || j.status !== "SUCCESS") throw new Error(`agy falhou: ${texto.slice(0, 300)}`);
  return String(j.response ?? "");
}
function extrairJson(texto: string): any {
  const m = texto.match(/```json\s*([\s\S]*?)```/) ?? texto.match(/```\s*([\s\S]*?)```/);
  return JSON.parse((m ? m[1] : texto).trim());
}

// ------------------------------------------------------------------ conferência automática
function conferir(itens: Json[], item: Json, recorte: Json, materia: Json) {
  const caps = new Set((materia.chapters as Json[]).map((c) => String(c.title)));
  const existentes = new Set((recorte.questions as Json[]).map((q) => norm(q.statement)));
  const vistos = new Set<string>();
  const ok: Json[] = [], motivos: string[] = [];
  for (const [i, q] of itens.entries()) {
    const erro = (m: string) => motivos.push(`item ${i + 1}: ${m}`);
    const opts = (q.options ?? []) as Json[];
    const corretas = opts.filter((o) => o.correct === true).length;
    const s = norm(q.statement);
    if (!s || s.length < 25) { erro("enunciado vazio ou curto"); continue; }
    if (existentes.has(s) || vistos.has(s)) { erro("enunciado repetido"); continue; }
    if (item.ce ? (q.format !== "TRUE_FALSE" || opts.length !== 2 || opts[0]?.key !== "C" || opts[1]?.key !== "E") : (q.format !== "MULTIPLE_CHOICE" || opts.length !== 5)) { erro("formato errado"); continue; }
    if (corretas !== 1) { erro(`${corretas} alternativas corretas`); continue; }
    if (!item.ce && new Set(opts.map((o) => norm(o.text))).size !== 5) { erro("alternativas repetidas"); continue; }
    if (!caps.has(String(q.section))) { erro("capítulo inexistente"); continue; }
    const exp = String(q.explanation ?? "");
    if (exp.length < 120) { erro("explicação curta"); continue; }
    if (/\b(letra|alternativa|op[cç][aã]o) [A-E]\b|^[A-E] (est[aá]|[eé]) corret/i.test(exp)) { erro("explicação cita letra"); continue; }
    if (/\b(conforme|segundo|de acordo com) a (mat[eé]ria|teoria)\b|na (mat[eé]ria|teoria) estudad/i.test(`${q.statement} ${opts.map((o) => o.text).join(" ")}`)) { erro("cita a matéria"); continue; }
    vistos.add(s);
    ok.push({ ...q, topico: q.topico ?? recorte.questions?.[0]?.topico, estilo: q.estilo ?? recorte.questions?.[0]?.estilo, sourceType: "AUTHORIAL", sourceUrl: null, board: null, agency: null, year: null });
  }
  // conferências do lote inteiro
  const problemasLote: string[] = [];
  if (item.ce) {
    const c = ok.filter((q) => q.options[0].correct).length;
    if (ok.length && (c / ok.length < 0.35 || c / ok.length > 0.65)) problemasLote.push(`Certo/Errado desequilibrado (${c} Certo de ${ok.length})`);
  } else {
    let longa = 0, curta = 0;
    for (const q of ok) {
      const l = (q.options as Json[]).map((o) => String(o.text).length);
      const k = (q.options as Json[]).findIndex((o) => o.correct);
      if (l[k] === Math.max(...l) && l.filter((x) => x === l[k]).length === 1) longa++;
      if (l[k] === Math.min(...l) && l.filter((x) => x === l[k]).length === 1) curta++;
    }
    if (longa > ok.length * 0.35) problemasLote.push(`a correta é a mais longa em ${longa} de ${ok.length}`);
    if (curta > ok.length * 0.3) problemasLote.push(`a correta é a mais curta em ${curta} de ${ok.length}`);
  }
  return { ok, motivos, problemasLote };
}

// ------------------------------------------------------------------ auditoria por outro modelo
async function auditar(itens: Json[], recorte: Json, materia: Json): Promise<Set<number>> {
  Deno.writeTextFileSync(`${WORK}/_trabalho/lote.json`, JSON.stringify(itens.map((q, i) => ({ n: i + 1, ...q })), null, 1));
  const prompt = `Você é um revisor rigoroso de questões de concurso (banca ${recorte.board}, assunto "${materia.title}").
Leia o arquivo lote.json desta pasta. Para CADA questão, confira:
1. o gabarito marcado está certo;
2. não há outra alternativa também correta (ou, em Certo/Errado, o item não é ambíguo);
3. o gabarito não depende de ponto em que a lei, a doutrina ou a gramática admitem dois entendimentos;
4. não há lei, artigo, prazo, valor ou súmula inventados ou errados;
5. o enunciado não entrega a resposta e nenhuma alternativa é absurda;
6. a explicação está de acordo com o gabarito.
Responda SÓ com um bloco \`\`\`json no formato {"remover": [números das questões com qualquer problema acima], "motivos": {"número": "motivo curto"}}.
Na dúvida, remova. Não use outras ferramentas além de ler o arquivo.`;
  // A cota semanal do Claude acaba bem antes da do Gemini (~2% por auditoria): o Opus audita 1 lote a
  // cada 4 e, nos outros, o Argon no esforço alto. Se a cota do Claude acabar, o Argon assume.
  estado.auditorias = (estado.auditorias ?? 0) + 1;
  const modelo = estado.auditorias % 4 === 1 ? AUD_MODEL : AUD_GEMINI;
  log(`   auditando com ${modelo}`);
  let resp: string;
  try { resp = await agy(modelo, prompt, 20); }
  catch (e) { if (e instanceof Cota && modelo !== AUD_GEMINI) resp = await agy(AUD_GEMINI, prompt, 20); else throw e; }
  const r = extrairJson(resp);
  const remover = new Set<number>((r.remover ?? []).map((x: any) => Number(x)));
  for (const [n, m] of Object.entries(r.motivos ?? {})) log(`   auditoria removeu ${n}: ${String(m).slice(0, 140)}`);
  return remover;
}

// ------------------------------------------------------------------ juntar ao recorte
function juntar(item: Json, novos: Json[], materia: Json): number {
  const recorte = JSON.parse(Deno.readTextFileSync(item.path));
  const backup = JSON.stringify(recorte);
  let candidatos = [...novos];
  for (let tentativa = 0; tentativa < 6 && candidatos.length; tentativa++) {
    const r = JSON.parse(backup);
    r.questions.push(...candidatos);
    r.version = Number(r.version ?? 1) + 1;
    try {
      validateBoardNote(r, materia);
      Deno.writeTextFileSync(item.path, JSON.stringify(r, null, 2) + "\n");
      return candidatos.length;
    } catch (e) {
      const problemas = ((e as { problems?: string[] }).problems ?? [String(e)]);
      const base = JSON.parse(backup).questions.length;
      const ruins = new Set(problemas.map((p) => Number(p.match(/questions\[(\d+)\]/)?.[1])).filter((n) => n >= base).map((n) => n - base));
      if (ruins.size === 0) { log(`   validador recusou o lote inteiro: ${problemas.slice(0, 3).join(" | ")}`); return 0; }
      candidatos = candidatos.filter((_, i) => !ruins.has(i));
    }
  }
  return 0;
}

// ------------------------------------------------------------------ publicar e gravar
async function publicarEGravar() {
  log("publicando a biblioteca e gravando no git...");
  const run = async (exe: string, a: string[]) => {
    const o = await new Deno.Command(exe, { args: a, cwd: ROOT, stdout: "piped", stderr: "piped" }).output();
    return { ok: o.success, txt: new TextDecoder().decode(o.stdout) + new TextDecoder().decode(o.stderr) };
  };
  const pub = await run(BASH, ["scripts/biblioteca/publicar.sh"]);
  if (!pub.ok) { log(`   publicação falhou: ${pub.txt.slice(-400)}`); return; }
  log(`   ${pub.txt.split("\n").find((l) => l.startsWith("Matérias")) ?? "publicado"}`);
  await run("git", ["add", "conteudo/recortes", "conteudo/entrada/agy"]);
  const c = await run("git", ["commit", "-q", "-m", `recortes: +${estado.aceitosDesdePublicacaoQuestoes ?? "?"} questões do gerador automático (Antigravity: Argon escreve, Opus audita)\n\nCo-Authored-By: Claude Opus 5.5 <noreply@anthropic.com>`]);
  if (!c.ok && !/nothing to commit/.test(c.txt)) { log(`   commit falhou: ${c.txt.slice(-300)}`); return; }
  const f = await run("git", ["fetch", "-q"]);
  const m = await run("git", ["merge", "-q", "--no-edit", "origin/main"]);
  if (!m.ok) { log(`   merge falhou (resolver à mão): ${m.txt.slice(-300)}`); return; }
  const p = await run("git", ["push", "-q", "origin", "HEAD:main"]);
  if (!p.ok) { log(`   push falhou: ${p.txt.slice(-300)}`); return; }
  await run("git", ["branch", "-f", "main", "HEAD"]);
  void f;
  estado.aceitosDesdePublicacao = 0;
  estado.aceitosDesdePublicacaoQuestoes = 0;
  salvarEstado();
  log("   publicado e gravado.");
}

// ------------------------------------------------------------------ um lote
async function umLote(item: Json): Promise<void> {
  const recorte = JSON.parse(Deno.readTextFileSync(item.path));
  const [dir, arq] = String(recorte.topic).split(".");
  const materia = JSON.parse(Deno.readTextFileSync(`${ROOT}conteudo/materias/${dir}/${arq}.json`));
  const est = (estado.pares[item.key] ??= { lotes: 0, aceitas: 0, falhasSeguidas: 0 });
  log(`lote para ${item.key} (${item.uso} editais, ${item.n} questões, ${item.ce ? "Certo/Errado" : "múltipla escolha"})`);
  let retorno = "", aprovados: Json[] = [];
  for (let tentativa = 1; tentativa <= 2; tentativa++) {
    const resp = await agy(GEN_MODEL, montarPedido(item, recorte, materia, retorno), 20);
    let itens: Json[];
    try { itens = extrairJson(resp); } catch { retorno = "A resposta não era um JSON válido."; log("   resposta sem JSON válido"); continue; }
    if (!Array.isArray(itens)) { retorno = "A resposta não era um array."; continue; }
    const c = conferir(itens, item, recorte, materia);
    log(`   ${itens.length} recebidas, ${c.ok.length} passaram na conferência${c.problemasLote.length ? `; problemas: ${c.problemasLote.join("; ")}` : ""}`);
    if (c.problemasLote.length && tentativa === 1) { retorno = c.problemasLote.join("\n") + "\n" + c.motivos.slice(0, 15).join("\n"); continue; }
    if (c.ok.length < LOTE * 0.5) { retorno = c.motivos.slice(0, 20).join("\n"); continue; }
    aprovados = c.ok;
    break;
  }
  est.lotes++; est.ultimo = Date.now();
  if (!aprovados.length) { est.falhasSeguidas++; salvarEstado(); log("   lote descartado"); return; }
  const remover = await auditar(aprovados, recorte, materia);
  const finais = aprovados.filter((_, i) => !remover.has(i + 1));
  const juntadas = juntar(item, finais, materia);
  Deno.writeTextFileSync(`${WORK}/aceitos/${dir}.${arq}--${normalizeBoard(recorte.board).replace(/\s+/g, "-")}--${Date.now()}.json`, JSON.stringify(finais, null, 1));
  est.aceitas += juntadas; est.falhasSeguidas = juntadas ? 0 : est.falhasSeguidas + 1;
  estado.totalAceitas = (estado.totalAceitas ?? 0) + juntadas;
  if (juntadas) { estado.aceitosDesdePublicacao = (estado.aceitosDesdePublicacao ?? 0) + 1; estado.aceitosDesdePublicacaoQuestoes = (estado.aceitosDesdePublicacaoQuestoes ?? 0) + juntadas; }
  salvarEstado();
  log(`   auditoria removeu ${remover.size}; ${juntadas} questões entraram no recorte (total do gerador: ${estado.totalAceitas})`);
  if ((estado.aceitosDesdePublicacao ?? 0) >= PUBLICAR_A_CADA) await publicarEGravar();
}

// ------------------------------------------------------------------ laço principal
if (args.has("--fila")) {
  for (const x of montarFila().slice(0, 40)) console.log(`${x.uso}\t${x.n}\t${x.ce ? "C/E" : "ME"}\t${x.key}`);
  Deno.exit(0);
}
log(`gerador iniciado (gera com ${GEN_MODEL}, audita com ${AUD_MODEL})`);
while (true) {
  const fila = montarFila();
  if (!fila.length) { log("fila vazia; esperando 1 hora"); await sleep(3_600_000); continue; }
  try {
    await umLote(fila[0]);
    if (args.has("--uma-vez")) break;
  } catch (e) {
    if (e instanceof Cota) { log(`cota esgotada; esperando 30 minutos (${e.message.slice(0, 120)})`); await sleep(1_800_000); continue; }
    log(`erro: ${String((e as Error).message ?? e).slice(0, 300)}; esperando 5 minutos`);
    const est = (estado.pares[fila[0].key] ??= { lotes: 0, aceitas: 0, falhasSeguidas: 0 });
    est.falhasSeguidas = (est.falhasSeguidas ?? 0) + 1; est.ultimo = Date.now(); salvarEstado();
    if (args.has("--uma-vez")) break;
    await sleep(300_000);
  }
}
if (args.has("--uma-vez") && (estado.aceitosDesdePublicacao ?? 0) > 0) log("teste concluído (sem publicar; publique com o gerador contínuo ou à mão)");
