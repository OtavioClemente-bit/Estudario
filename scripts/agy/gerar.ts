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
// O Argon tem cota semanal própria e pequena (31% em 1 hora): escreve com o Flash alto, que usa a cota grande do Gemini.
const GEN_MODEL = "gemini-3.8-flash-high";
const AUD_MODEL = "claude-opus-5-5-medium";
const AUD_GEMINI = "gemini-4-argon-high";
const AUD_FLASH = "gemini-3.8-flash-high";
const META = 150;            // um recorte com isso já está bom; passa para o próximo
// 25 por resposta: o Flash corta respostas maiores (com 50 dava "exceeded the output token limit").
const LOTE = 25;
const MIN_EDITAIS = 15;
const PUBLICAR_A_CADA = 5;   // lotes aceitos entre uma publicação e outra
const WORK = `${ROOT}conteudo/entrada/agy`;
const args = new Set(Deno.args);

type Json = Record<string, any>;
const sleep = (ms: number) => new Promise((r) => setTimeout(r, ms));
const norm = (s: string) => String(s ?? "").toLowerCase().normalize("NFD").replace(/[\u0300-\u036f]/g, "").replace(/[^a-z0-9]+/g, " ").trim();
// --api: segundo gerador em paralelo, que escreve pela API grátis do Google (Flash) e audita pelo agy.
const MODO_API = args.has("--api");
const TRAB = `${WORK}/_trabalho${MODO_API ? "-api" : ""}`;
Deno.mkdirSync(TRAB, { recursive: true });
Deno.mkdirSync(`${WORK}/travas`, { recursive: true });
Deno.mkdirSync(`${WORK}/aceitos`, { recursive: true });
function log(msg: string) {
  const line = `[${new Date().toLocaleString("pt-BR")}] ${MODO_API ? "[api] " : ""}${msg}`;
  console.log(line);
  Deno.writeTextFileSync(`${WORK}/log.txt`, line + "\n", { append: true });
}
const estadoPath = `${WORK}/estado${MODO_API ? "-api" : ""}.json`;
let estado: Json = { pares: {}, aceitosDesdePublicacao: 0, totalAceitas: 0 };
try { estado = JSON.parse(Deno.readTextFileSync(estadoPath)); } catch { /* primeira vez */ }
const salvarEstado = () => Deno.writeTextFileSync(estadoPath, JSON.stringify(estado, null, 1));

// ------------------------------------------------------------------ travas (os dois geradores não pegam o mesmo recorte)
const arquivoTrava = (key: string) => `${WORK}/travas/${key.replace(/[^a-z0-9.-]+/gi, "_")}`;
function travado(key: string) {
  try { return Date.now() - Deno.statSync(arquivoTrava(key)).mtime!.getTime() < 3_600_000; } catch { return false; }
}
const travar = (key: string) => Deno.writeTextFileSync(arquivoTrava(key), MODO_API ? "api" : "agy");
const destravar = (key: string) => { try { Deno.removeSync(arquivoTrava(key)); } catch { /* já saiu */ } };

// ------------------------------------------------------------------ API grátis do Google (gerador --api)
const API_MODELS = ["gemini-3.8-flash", "gemini-3.7-flash", "gemini-3.6-flash"];
async function geminiApi(prompt: string): Promise<string> {
  const key = Deno.readTextFileSync(`${ROOT}.env.google`).match(/GEMINI_API_KEY=(.+)/)![1].trim();
  let ultimo = "";
  for (const model of API_MODELS) {
    const r = await fetch(`https://generativelanguage.googleapis.com/v1beta/models/${model}:generateContent`, {
      method: "POST", headers: { "content-type": "application/json", "x-goog-api-key": key },
      body: JSON.stringify({ contents: [{ role: "user", parts: [{ text: prompt }] }], generationConfig: { maxOutputTokens: 65000 } }),
    });
    const j = await r.json().catch(() => ({}));
    if (r.status === 429) { ultimo = `${model}: cota`; continue; }
    if (!r.ok) { ultimo = `${model}: ${r.status}`; continue; }
    const txt = (j.candidates?.[0]?.content?.parts ?? []).map((p: Json) => p.text ?? "").join("");
    if (txt) { log(`   escrito por ${model}`); return txt; }
    ultimo = `${model}: resposta vazia`;
  }
  if (/cota/.test(ultimo)) throw new Cota(ultimo);
  throw new Error(`API do Google indisponível (${ultimo})`);
}

// ------------------------------------------------------------------ fila
function walk(dir: string, out: string[] = []) {
  for (const e of Deno.readDirSync(dir)) {
    if (e.isDirectory) walk(`${dir}/${e.name}`, out);
    else if (e.name.endsWith(".json")) out.push(`${dir}/${e.name}`);
  }
  return out;
}
function filas() {
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
  const nomes = new Map<string, Map<string, number>>();
  for (const e of Deno.readDirSync(`${ROOT}conteudo/editais`)) {
    if (!e.name.endsWith(".json")) continue;
    const ed = JSON.parse(Deno.readTextFileSync(`${ROOT}conteudo/editais/${e.name}`));
    const bruto = String(ed.banca ?? "").trim();
    const b = normalizeBoard(bruto);
    if (!b) continue;
    const m = nomes.get(b) ?? new Map<string, number>();
    m.set(bruto, (m.get(bruto) ?? 0) + 1);
    nomes.set(b, m);
    const ids = new Set<string>();
    for (const d of ed.disciplinas ?? []) for (const t of d.topicos ?? []) if (t.topico) ids.add(t.topico);
    for (const id of ids) uso.set(`${id}|${b}`, (uso.get(`${id}|${b}`) ?? 0) + 1);
  }
  const agora = Date.now();
  // par que falhou 3 vezes seguidas descansa um dia
  const descansando = (key: string) => {
    const est = estado.pares[key] ?? {};
    return (est.falhasSeguidas ?? 0) >= 3 && agora - (est.ultimo ?? 0) < 86_400_000;
  };
  const ampliar = [...recortes.entries()]
    .map(([key, r]) => ({ key, ...r, novo: false, uso: uso.get(key) ?? 0 }))
    .filter((x) => x.uso >= MIN_EDITAIS && x.n < META && !travado(x.key) && !descansando(x.key))
    .sort((a, b) => b.uso * (META - b.n) - a.uso * (META - a.n));
  // Recortes que ainda não existem: banca × matéria que aparece em pelo menos 20 editais.
  const novos = [...uso.entries()]
    .filter(([key, n]) => n >= 20 && !recortes.has(key) && !key.endsWith("|geral") && !key.startsWith("atualidades.") && !travado(key) && !descansando(key))
    .map(([key, n]) => {
      const [topic, b] = key.split("|");
      const [dir, arq] = topic.split(".");
      const board = [...(nomes.get(b) ?? new Map()).entries()].sort((x, y) => y[1] - x[1])[0]?.[0] ?? b;
      const pasta = b.replace(/\s+/g, "-");
      return { key, topic, board, pasta, novo: true, n: 0, uso: n, ce: ["cebraspe", "instituto quadrix"].includes(b),
        path: `${ROOT}conteudo/recortes/${pasta}/${topic}.json`, materiaPath: `${ROOT}conteudo/materias/${dir}/${arq}.json` };
    })
    // matéria tem de existir; e não pega o que o Codex já entregou no lote 3
    .filter((x) => { try { Deno.statSync(x.materiaPath); } catch { return false; } try { Deno.statSync(`${ROOT}conteudo/entrada/recortes-luna-3/${x.pasta}/${x.topic}.json`); return false; } catch { return true; } })
    .sort((a, b) => b.uso - a.uso);
  return { ampliar, novos };
}
function montarFila() { return montarFilas().ampliar; }
function montarFilas(): { ampliar: Json[]; novos: Json[] } { return filas(); }

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

${item.novo ? `ENTREGA: responda SÓ com um bloco de código \`\`\`json contendo UM objeto, nada fora do bloco:
{
  "incidence": "ALTA" | "MEDIA" | "BAIXA",
  "howItFalls": "400 a 2.000 caracteres: como a banca ${recorte.board} cobra esta matéria (formato, comandos típicos, tipo de pegadinha), com 2 exemplos curtos de enunciado escritos por você; sem estatística inventada",
  "tips": [4 a 6 dicas de prova específicas, uma frase cada],
  "traps": [4 a 6 pegadinhas específicas, uma frase cada],
  "questions": [array com as ${LOTE} questões]
}
Não use ferramentas nem crie arquivos; apenas escreva a resposta.

Formato de cada questão (chaves exatamente assim):` : `ENTREGA: responda SÓ com um bloco de código \`\`\`json contendo um array de ${LOTE} objetos, nada fora do bloco.
Não use ferramentas nem crie arquivos; apenas escreva a resposta.

Formato de cada objeto (chaves exatamente assim):`}
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
  const pedido = `${TRAB}/pedido.txt`;
  Deno.writeTextFileSync(pedido, prompt);
  // O Flash, mandado ler um arquivo, tenta rodar um comando (bloqueado no modo automático) e volta vazio:
  // pedido de até ~28 mil caracteres vai na própria linha de comando; maior que isso (só a auditoria), por arquivo.
  const pedidoLinha = prompt.length <= 28_000 ? prompt
    : "Leia o arquivo pedido.txt desta pasta com a ferramenta de leitura de arquivos (não rode comandos) e cumpra exatamente o que ele pede.";
  const cmd = new Deno.Command(AGY, {
    args: ["-p", pedidoLinha,
      "--model", model, "--output-format", "json", "--print-timeout", `${minutos}m`, "--disable-slash-commands"],
    cwd: TRAB, stdout: "piped", stderr: "piped",
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
  // As questões vão dentro do pedido (sem ferramentas), só com o que o revisor precisa ler.
  const compacto = JSON.stringify(itens.map((q, i) => ({ n: i + 1, enunciado: q.statement,
    alternativas: (q.options as Json[]).map((o) => `${o.key}) ${o.text}${o.correct ? "  [GABARITO]" : ""}`), explicacao: q.explanation })));
  Deno.writeTextFileSync(`${TRAB}/lote.json`, compacto);
  const prompt = `Você é um revisor rigoroso de questões de concurso (banca ${recorte.board}, assunto "${materia.title}").
As questões estão no JSON abaixo. Para CADA questão, confira:
1. o gabarito marcado está certo;
2. não há outra alternativa também correta (ou, em Certo/Errado, o item não é ambíguo);
3. o gabarito não depende de ponto em que a lei, a doutrina ou a gramática admitem dois entendimentos;
4. não há lei, artigo, prazo, valor ou súmula inventados ou errados;
5. o enunciado não entrega a resposta e nenhuma alternativa é absurda;
6. a explicação está de acordo com o gabarito.
Responda SÓ com um bloco \`\`\`json no formato {"remover": [números das questões com qualquer problema acima], "motivos": {"número": "motivo curto"}}.
Na dúvida, remova. Não use ferramentas.

QUESTÕES:
${compacto}`;
  // A cota semanal do Claude acaba bem antes da do Gemini (~2% por auditoria): o Opus audita 1 lote a
  // cada 4 e, nos outros, o Argon no esforço alto. Se a cota do Claude acabar, o Argon assume.
  estado.auditorias = (estado.auditorias ?? 0) + 1;
  // Sem Claude (a cota dele acabou em 1 hora). Argon e Flash alto se alternam; se o Argon acabar, o Flash assume.
  const modelo = estado.auditorias % 2 === 1 ? AUD_GEMINI : AUD_FLASH;
  log(`   auditando com ${modelo}`);
  let resp: string;
  try { resp = await agy(modelo, prompt, 20); }
  catch (e) { if (e instanceof Cota && modelo !== AUD_FLASH) resp = await agy(AUD_FLASH, prompt, 20); else throw e; }
  const r = extrairJson(resp);
  const remover = new Set<number>((r.remover ?? []).map((x: any) => Number(x)));
  for (const [n, m] of Object.entries(r.motivos ?? {})) log(`   auditoria removeu ${n}: ${String(m).slice(0, 140)}`);
  return remover;
}

// ------------------------------------------------------------------ juntar ao recorte
function juntar(item: Json, novos: Json[], materia: Json): number {
  // Recorte novo: começa do cabeçalho escrito pelo modelo, sem questões, e nasce na versão 1.
  const recorte = item.novo ? { ...item.base, version: 0, questions: [] } : JSON.parse(Deno.readTextFileSync(item.path));
  if (item.novo) Deno.mkdirSync(item.path.replace(/\/[^/]+$/, ""), { recursive: true });
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
  travar(item.key);
  try { await loteTravado(item); } finally { destravar(item.key); }
}
async function loteTravado(item: Json): Promise<void> {
  // Recorte novo ainda não tem arquivo: trabalha sobre um recorte vazio com a banca e a matéria certas.
  const recorte = item.novo
    ? { topic: item.topic, board: item.board, role: null, questions: [] as Json[] }
    : JSON.parse(Deno.readTextFileSync(item.path));
  const [dir, arq] = String(recorte.topic).split(".");
  const materia = JSON.parse(Deno.readTextFileSync(`${ROOT}conteudo/materias/${dir}/${arq}.json`));
  const est = (estado.pares[item.key] ??= { lotes: 0, aceitas: 0, falhasSeguidas: 0 });
  log(`${item.novo ? "RECORTE NOVO" : "lote"} para ${item.key} (${item.uso} editais, ${item.n} questões, ${item.ce ? "Certo/Errado" : "múltipla escolha"})`);
  let retorno = "", aprovados: Json[] = [];
  for (let tentativa = 1; tentativa <= 2; tentativa++) {
    const pedidoTxt = montarPedido(item, recorte, materia, retorno);
    const resp = MODO_API ? await geminiApi(pedidoTxt) : await agy(GEN_MODEL, pedidoTxt, 20);
    let itens: Json[];
    let bruto: any;
    try { bruto = extrairJson(resp); } catch { retorno = "A resposta não era um JSON válido."; log("   resposta sem JSON válido"); continue; }
    if (item.novo) {
      // Cabeçalho do recorte novo: confere o mínimo antes de olhar as questões.
      const h = bruto ?? {};
      const cab = ["ALTA", "MEDIA", "BAIXA"].includes(h.incidence) && String(h.howItFalls ?? "").length >= 400 &&
        Array.isArray(h.tips) && h.tips.length >= 3 && Array.isArray(h.traps) && h.traps.length >= 3 &&
        !/\b(mat[eé]ria|teoria) (estudada|apresentada)|conforme a mat[eé]ria/i.test(JSON.stringify([h.howItFalls, h.tips, h.traps]));
      if (!cab) { retorno = "O cabeçalho veio incompleto: incidence ALTA/MEDIA/BAIXA, howItFalls com 400+ caracteres, 4 a 6 tips e 4 a 6 traps, sem citar 'a matéria'."; log("   cabeçalho incompleto"); continue; }
      item.base = { topic: item.topic, board: item.board, role: null, incidence: h.incidence, howItFalls: String(h.howItFalls),
        tips: h.tips.map(String), traps: h.traps.map(String) };
      itens = h.questions;
    } else itens = bruto;
    if (!Array.isArray(itens)) { retorno = "As questões não vieram num array."; continue; }
    const c = conferir(itens, item, recorte, materia);
    log(`   ${itens.length} recebidas, ${c.ok.length} passaram na conferência${c.problemasLote.length ? `; problemas: ${c.problemasLote.join("; ")}` : ""}`);
    if (c.problemasLote.length && tentativa === 1) { retorno = c.problemasLote.join("\n") + "\n" + c.motivos.slice(0, 15).join("\n"); continue; }
    if (c.ok.length < LOTE * 0.5) { retorno = c.motivos.slice(0, 20).join("\n"); continue; }
    aprovados = c.ok;
    break;
  }
  // Recorte novo precisa de 40+ questões, mas o Flash só aguenta 25 por resposta: pede mais 25 (sem cabeçalho),
  // mostrando as já escritas para não repetir.
  if (item.novo && aprovados.length) {
    const mais = { ...item, novo: false };
    const base2 = { ...recorte, questions: aprovados };
    try {
      const pedido2 = montarPedido(mais, base2, materia);
      const resp2 = MODO_API ? await geminiApi(pedido2) : await agy(GEN_MODEL, pedido2, 20);
      const it2 = extrairJson(resp2);
      if (Array.isArray(it2)) {
        const c2 = conferir(it2, mais, base2, materia);
        log(`   segunda parte: ${it2.length} recebidas, ${c2.ok.length} passaram na conferência`);
        aprovados = [...aprovados, ...c2.ok];
      }
    } catch (e) { if (e instanceof Cota) throw e; log(`   segunda parte falhou: ${String((e as Error).message ?? e).slice(0, 120)}`); }
  }
  est.lotes++; est.ultimo = Date.now();
  if (!aprovados.length) { est.falhasSeguidas++; salvarEstado(); log("   lote descartado"); return; }
  const remover = await auditar(aprovados, recorte, materia);
  const finais = aprovados.filter((_, i) => !remover.has(i + 1));
  // O validador exige pelo menos 40 questões num recorte; recorte novo que ficou abaixo disso tenta outra vez depois.
  if (item.novo && finais.length < 40) { est.falhasSeguidas++; salvarEstado(); log(`   recorte novo ficou com ${finais.length} questões depois da auditoria; tenta de novo depois`); return; }
  const juntadas = juntar(item, finais, materia);
  Deno.writeTextFileSync(`${WORK}/aceitos/${dir}.${arq}--${normalizeBoard(recorte.board).replace(/\s+/g, "-")}--${Date.now()}.json`, JSON.stringify(finais, null, 1));
  est.aceitas += juntadas; est.falhasSeguidas = juntadas ? 0 : est.falhasSeguidas + 1;
  estado.totalAceitas = (estado.totalAceitas ?? 0) + juntadas;
  if (juntadas) { estado.aceitosDesdePublicacao = (estado.aceitosDesdePublicacao ?? 0) + 1; estado.aceitosDesdePublicacaoQuestoes = (estado.aceitosDesdePublicacaoQuestoes ?? 0) + juntadas; }
  salvarEstado();
  log(`   auditoria removeu ${remover.size}; ${juntadas} questões entraram no recorte (total do gerador: ${estado.totalAceitas})`);
  // Só o gerador principal publica e grava (o da API só junta ao recorte; o principal leva junto).
  if (!MODO_API && (estado.aceitosDesdePublicacao ?? 0) >= PUBLICAR_A_CADA) await publicarEGravar();
}

// ------------------------------------------------------------------ laço principal
if (args.has("--fila")) {
  const f = montarFilas();
  console.log("AMPLIAR"); for (const x of f.ampliar.slice(0, 20)) console.log(`${x.uso}\t${x.n}\t${x.ce ? "C/E" : "ME"}\t${x.key}`);
  console.log(`NOVOS (${f.novos.length})`); for (const x of f.novos.slice(0, 20)) console.log(`${x.uso}\t${x.ce ? "C/E" : "ME"}\t${x.board}\t${x.topic}`);
  Deno.exit(0);
}
log(`gerador ${MODO_API ? "da API " : ""}iniciado (gera com ${MODO_API ? API_MODELS.join("/") : GEN_MODEL}, audita com ${AUD_MODEL}/${AUD_GEMINI}; alterna ampliar e recorte novo)`);
while (true) {
  // Alterna: um lote ampliando recorte existente, o seguinte criando recorte novo (se uma fila acabar, usa a outra).
  const f = montarFilas();
  estado.vez = estado.vez === "novo" ? "ampliar" : "novo";
  const ordem = estado.vez === "novo" ? [f.novos, f.ampliar] : [f.ampliar, f.novos];
  const fila = ordem[0].length ? ordem[0] : ordem[1];
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
