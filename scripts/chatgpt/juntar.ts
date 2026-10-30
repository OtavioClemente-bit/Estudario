// Junta aos recortes as questões dos arquivos do ChatGPT (conteudo/entrada/chatgpt) que já foram auditados
// ("auditado": true). Só converte o formato: não escreve, não completa nem embaralha questão. Questão que não
// passa nas conferências é deixada de fora (e contada). Sem --gravar só mostra o que faria.
//   deno run -A scripts/chatgpt/juntar.ts [--gravar]
import { validateBoardNote } from "../../supabase/functions/_shared/library.ts";
const ROOT = new URL("../../", import.meta.url).pathname.replace(/^\/([A-Za-z]:)/, "$1");
const DIR = `${ROOT}conteudo/entrada/chatgpt`;
const GRAVAR = Deno.args.includes("--gravar");
type Json = Record<string, any>;
const ler = (p: string) => JSON.parse(Deno.readTextFileSync(p).replace(/^﻿/, ""));
const norm = (s: unknown) => String(s ?? "").toLowerCase().normalize("NFD").replace(/[̀-ͯ]/g, "").replace(/[^a-z0-9]+/g, " ").trim();
const LETRAS = ["A", "B", "C", "D", "E"];

// Lista de questões, esteja ela na raiz ou dentro das seções.
function questoesDe(j: Json): Json[] {
  const q = j.questoes ?? j.questions;
  if (Array.isArray(q) && q.length) return q;
  return ((j.secoes ?? j.sections ?? []) as Json[]).flatMap((s) => ((s.questoes ?? s.questions ?? []) as Json[]).map((x) => ({ secao: s.numero ?? s.id, secao_titulo: s.titulo ?? s.title, ...x })));
}
// Seções declaradas: número → título.
function secoesDe(j: Json): Map<string, string> {
  const m = new Map<string, string>();
  for (const s of (j.secoes ?? j.sections ?? []) as Json[]) {
    if (typeof s === "string") m.set(String(m.size + 1), s);
    else m.set(String(s.numero ?? s.id ?? s.number ?? m.size + 1), String(s.titulo ?? s.title ?? s.nome ?? ""));
  }
  return m;
}
// Escolhe o capítulo da matéria: título igual, título parecido (mais palavras em comum) ou mesma posição.
function capitulo(q: Json, secoes: Map<string, string>, caps: string[]): string | null {
  const bruto = q.section ?? q.secao_titulo ?? q.secao ?? q.secao_id ?? q.sectionIndex ?? q.topico;
  if (bruto == null) return null;
  const titulo = secoes.get(String(bruto)) || String(bruto);
  const t = norm(titulo).replace(/^\d+\s*/, "");
  const exato = caps.find((c) => norm(c).replace(/^\d+\s*/, "") === t);
  if (exato) return exato;
  const pal = new Set(t.split(" ").filter((w) => w.length > 3));
  let melhor: string | null = null, nota = 0;
  for (const c of caps) {
    const n = norm(c).split(" ").filter((w) => w.length > 3 && pal.has(w)).length;
    if (n > nota) { nota = n; melhor = c; }
  }
  if (melhor && nota >= 2) return melhor;
  const idx = Number(String(bruto).match(/\d+/)?.[0]);
  if (secoes.size === caps.length && idx >= 1 && idx <= caps.length) return caps[idx - 1];
  return null;
}
// Converte uma questão para o formato do app.
function converter(q: Json, ce: boolean): Json | string {
  const enun = String(q.statement ?? q.enunciado ?? "").trim();
  const exp = [q.explanation ?? q.comentario ?? q.justificativa ?? q.explicacao ?? q.resolucao, q.fundamento ?? q.fundamentacao ?? q.fundamento_legal ?? q.referencia]
    .filter(Boolean).map(String).join(" Fundamento: ").trim();
  const gab = String(q.gabarito ?? q.resposta_correta ?? q.answer ?? q.correctOption ?? "").trim();
  let opts: Json[];
  if (ce) {
    let certo: boolean | null = null;
    if (Array.isArray(q.options) && q.options[0]?.text) certo = q.options.find((o: Json) => o.correct)?.key === "C" || /^certo$/i.test(q.options.find((o: Json) => o.correct)?.text ?? "");
    if (gab) certo = /^(c|certo|v|verdadeiro|true)$/i.test(gab) ? true : /^(e|errado|f|falso|false)$/i.test(gab) ? false : certo;
    if (certo === null) return "gabarito ilegível";
    opts = [{ key: "C", text: "Certo", correct: certo }, { key: "E", text: "Errado", correct: !certo }];
  } else {
    const alt = q.options ?? q.alternativas;
    let lista: { key: string; text: string; correct?: boolean }[];
    if (Array.isArray(alt)) lista = alt.map((a: any, i: number) => typeof a === "string" ? { key: LETRAS[i], text: a.replace(/^[A-E][).:-]\s*/, "") } : { key: String(a.key ?? a.letra ?? LETRAS[i]), text: String(a.text ?? a.texto ?? ""), correct: a.correct });
    else if (alt && typeof alt === "object") lista = Object.entries(alt).map(([k, v]) => ({ key: k, text: String(v) }));
    else return "sem alternativas";
    if (lista.length !== 5) return "não tem 5 alternativas";
    const letra = gab.match(/^[A-E]\b/i)?.[0]?.toUpperCase();
    opts = lista.map((o, i) => ({ key: LETRAS[i], text: o.text.trim(), correct: letra ? o.key.toUpperCase() === letra : o.correct === true }));
  }
  if (opts.filter((o) => o.correct).length !== 1) return "não tem exatamente 1 correta";
  if (!ce && new Set(opts.map((o) => o.text.toLowerCase().trim())).size !== 5) return "alternativas repetidas";
  if (enun.length < 25) return "enunciado curto";
  if (exp.length < 120) return "explicação curta";
  if (/\b(letra|alternativa|op[cç][aã]o) [A-E]\b/i.test(exp)) return "explicação cita letra";
  const dif = String(q.difficulty ?? q.dificuldade ?? "MEDIA").toUpperCase().normalize("NFD").replace(/[̀-ͯ]/g, "");
  return {
    statement: enun, format: ce ? "TRUE_FALSE" : "MULTIPLE_CHOICE",
    difficulty: ["FACIL", "MEDIA", "DIFICIL"].includes(dif) ? dif : "MEDIA",
    options: opts, explanation: exp, errorConceptKey: null,
    sourceType: "AUTHORIAL", sourceUrl: null, board: null, agency: null, year: null,
  };
}

const total: Record<string, number> = {};
const conta = (m: string) => (total[m] = (total[m] ?? 0) + 1);
let juntadas = 0;
for (const e of [...Deno.readDirSync(DIR)].filter((e) => /\.chatgpt-\d+\.json$/.test(e.name)).sort((a, b) => a.name.localeCompare(b.name))) {
  const arq = `${DIR}/${e.name}`;
  let j: Json;
  try { j = ler(arq); } catch { console.log(`✗ ${e.name}: JSON quebrado`); continue; }
  if (j.auditado !== true || j.juntado) continue;
  const [pasta, area, ...resto] = e.name.replace(/\.chatgpt-\d+\.json$/, "").split(".");
  const topic = `${area}.${resto.join(".")}`;
  const rPath = `${ROOT}conteudo/recortes/${pasta}/${topic}.json`;
  const mPath = `${ROOT}conteudo/materias/${area}/${resto.join(".")}.json`;
  let recorte: Json, materia: Json;
  try { recorte = ler(rPath); materia = ler(mPath); } catch { console.log(`✗ ${e.name}: recorte ou matéria não existe`); continue; }
  const caps = (materia.chapters as Json[]).map((c) => String(c.title));
  const ce = (recorte.questions as Json[])[0]?.format === "TRUE_FALSE" || ["cebraspe", "quadrix"].includes(pasta) && !(recorte.questions as Json[]).some((q) => q.format === "MULTIPLE_CHOICE");
  const secoes = secoesDe(j);
  const vistos = new Set((recorte.questions as Json[]).map((q) => norm(q.statement)));
  const base = recorte.questions[0] ?? {};
  const novas: Json[] = [];
  const motivos: Record<string, number> = {};
  for (const q of questoesDe(j)) {
    // Certo/Errado pela própria questão (gabarito "Certo"/"Errado" ou alternativas Certo/Errado), senão pelo recorte.
    const alts = q.alternativas ?? q.options;
    const ceQ = q.format ? q.format === "TRUE_FALSE"
      : /^(certo|errado)$/i.test(String(q.gabarito ?? q.resposta_correta ?? "")) || (Array.isArray(alts) && alts.length === 2) || ce;
    const r = converter(q, ceQ);
    const cap = capitulo(q, secoes, caps);
    const motivo = typeof r === "string" ? r : !cap ? "capítulo não identificado" : vistos.has(norm(r.statement)) ? "repetida" : null;
    if (motivo) { motivos[motivo] = (motivos[motivo] ?? 0) + 1; conta(motivo); continue; }
    const nova = r as Json;
    vistos.add(norm(nova.statement));
    novas.push({ topico: base.topico, estilo: base.estilo, ...nova, section: cap });
  }
  // O validador da biblioteca tem a última palavra; o que ele recusar sai.
  let candidatas = novas;
  for (let t = 0; t < 6 && candidatas.length; t++) {
    const r = { ...recorte, questions: [...recorte.questions, ...candidatas], version: Number(recorte.version ?? 1) + 1 };
    try {
      validateBoardNote(r, materia);
      if (GRAVAR) {
        Deno.writeTextFileSync(rPath, JSON.stringify(r, null, 2) + "\n");
        Deno.writeTextFileSync(arq, JSON.stringify({ juntado: true, ...j }, null, 2) + "\n");
      }
      break;
    } catch (err) {
      const ruins = new Set(((err as Json).problems ?? []).map((p: string) => Number(p.match(/questions\[(\d+)\]/)?.[1]) - recorte.questions.length).filter((n: number) => n >= 0));
      if (!ruins.size) { console.log(`✗ ${e.name}: validador recusou tudo: ${String((err as Json).problems?.slice(0, 2) ?? err)}`); candidatas = []; break; }
      conta("recusada pelo validador");
      candidatas = candidatas.filter((_, i) => !ruins.has(i));
    }
  }
  juntadas += candidatas.length;
  console.log(`${candidatas.length ? "✓" : "✗"} ${e.name}: +${candidatas.length} ${JSON.stringify(motivos)}`);
}
console.log(`\n${GRAVAR ? "juntadas" : "juntaria"}: ${juntadas}; fora: ${JSON.stringify(total)}`);
