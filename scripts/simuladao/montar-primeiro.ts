// Simuladão 1 dos editais que ainda não têm simulado (AGENTS.md, 5ª exceção, 24/11/2026).
// Monta conteudo/simulados/<edital>.simulado-1.json escolhendo questões já publicadas e conferidas, na
// quantidade de questões por disciplina da prova real (campo `questoes` das disciplinas do edital, lido à mão
// dos PDFs). Depois disso o montar.ts faz o Simuladão 2, 3... todo mês na mesma estrutura.
// Ordem de escolha, por tópico do edital: recorte da banca do edital; recortes de outras bancas no mesmo
// formato; questões da própria matéria no mesmo formato. Sem questão no tópico, outro tópico da mesma
// disciplina. Não escreve, não altera nem completa o texto de nenhuma questão; faltando mais de 5%, não grava.
//
//   deno run --allow-read --allow-write=conteudo/simulados scripts/simuladao/montar-primeiro.ts [--edital <id>] [--limite N] [--seco]
const existsSync = (p: string) => { try { Deno.statSync(p); return true; } catch { return false; } };
type Json = Record<string, unknown>;
type Option = { key: string; text: string; correct?: boolean };
type Cand = { q: Json; fonte: string; materia: string; rank: number };

const ROOT = new URL("../../", import.meta.url).pathname.replace(/^\/([A-Za-z]:)/, "$1");
const EDITAIS = `${ROOT}conteudo/editais`, SIMULADOS = `${ROOT}conteudo/simulados`, RECORTES = `${ROOT}conteudo/recortes`, MATERIAS = `${ROOT}conteudo/materias`;
const args = Deno.args;
const only = args.includes("--edital") ? args[args.indexOf("--edital") + 1] : null;
const limite = args.includes("--limite") ? Number(args[args.indexOf("--limite") + 1]) : Infinity;
const dry = args.includes("--seco");
const MAX_FALTA = 0.05;
const ler = (p: string) => JSON.parse(Deno.readTextFileSync(p).replace(/^﻿/, "")) as Json;

const slug = (s: string) => s.normalize("NFD").replace(/[̀-ͯ]/g, "").toLowerCase().replace(/[^a-z0-9]+/g, "-").replace(/^-|-$/g, "");
const bancaFolders = [...Deno.readDirSync(RECORTES)].filter((e) => e.isDirectory).map((e) => e.name);
const folderOf = (banca: string) => { const s = slug(banca); return bancaFolders.find((f) => f === s) ?? bancaFolders.find((f) => s.startsWith(f) || f.startsWith(s.split("-")[0])) ?? null; };
function seedOf(text: string): number { let h = 2166136261; for (let i = 0; i < text.length; i++) h = Math.imul(h ^ text.charCodeAt(i), 16777619); return h >>> 0; }
function questionKey(text: string): string { const l = text.toLowerCase().normalize("NFD").replace(/[^a-z0-9]/g, ""); return String(seedOf(l)); }
const formatOf = (q: Json) => (q.format === "CERTO_ERRADO" || q.format === "TRUE_FALSE" ? "CERTO_ERRADO" : "MULTIPLE_CHOICE");
const valid = (q: Json) => Array.isArray(q.options) && (q.options as Option[]).filter((o) => o.correct).length === 1 && String(q.statement ?? "").length >= 20;
// Questão que só faz sentido dentro da matéria ("o decreto estudado", "neste capítulo") fica fora de prova solta.
const contextual = /\b(estudad[oa]s? (nesta|neste|na|no) |o decreto estudado|a lei estudada|a norma estudada|nesta mat[eé]ria|neste cap[ií]tulo|desta mat[eé]ria|do cap[ií]tulo)/i;
// Conferência de 24/11: fora da prova as que citam norma de 2025/2026 (as mais inventadas, ex.: "EC 141") e as que
// citam o edital ou a prova de outro concurso na explicação ("edital da PF").
const normaRecente = /\b(EC|Emenda Constitucional|Lei( Complementar)?|Decreto|Resolu[çc][aã]o|Portaria|Instru[çc][aã]o Normativa)\b[^;\n]{0,40}(\/\s?202[56]|de 202[56])/i;
const outroConcurso = /\b(edital|prova|avalia[çc][aã]o) d[aoe] [A-Z]{2,}/;
const fora = (q: Json) => contextual.test(String(q.statement)) || normaRecente.test(String(q.statement) + " " + String(q.explanation ?? "")) || outroConcurso.test(String(q.explanation ?? ""));
const palavras = (s: string) => new Set(s.toLowerCase().normalize("NFD").replace(/[̀-ͯ]/g, "").split(/[^a-z0-9]+/).filter((w) => w.length >= 5));
// Quase iguais: muitas palavras longas em comum com uma questão já escolhida.
function parecida(q: Json, escolhidas: Set<string>[]): boolean {
  const p = palavras(String(q.statement));
  if (p.size < 4) return false;
  return escolhidas.some((e) => { let c = 0; for (const w of p) if (e.has(w)) c++; return c / Math.min(p.size, e.size) >= 0.6; });
}
// Item que se entrega pela palavra absoluta ("sempre", "exclusivamente"): no máximo ~15% da prova.
const absoluto = (q: Json) => /\b(sempre|nunca|jamais|exclusivamente|somente|apenas|absolut[oa]|em qualquer hip[óo]tese|todo e qualquer)\b/i.test(String(q.statement));
const certaMaisLonga = (q: Json) => { const o = q.options as Option[]; const c = o.find((x) => x.correct); return !!c && o.every((x) => x === c || c.text.length > x.text.length); };

const cache = new Map<string, Json[]>();
function questoes(path: string): Json[] {
  if (!cache.has(path)) { let l: Json[] = []; try { l = (ler(path).questions as Json[]) ?? []; } catch { /* sem arquivo */ } cache.set(path, l); }
  return cache.get(path)!;
}
const materiaPath = (id: string) => { const [a, ...b] = id.split("."); return `${MATERIAS}/${a}/${b.join(".")}.json`; };

const candCache = new Map<string, Cand[]>();
function candidatos(materia: string, banca: string | null, formato: string): Cand[] {
  const k = `${materia}|${banca}|${formato}`;
  if (!candCache.has(k)) candCache.set(k, candidatosSemCache(materia, banca, formato));
  return candCache.get(k)!;
}
function candidatosSemCache(materia: string, banca: string | null, formato: string): Cand[] {
  const out: Cand[] = [];
  if (banca) for (const q of questoes(`${RECORTES}/${banca}/${materia}.json`)) out.push({ q, fonte: `recorte ${banca}`, materia, rank: 0 });
  for (const f of bancaFolders) if (f !== banca) for (const q of questoes(`${RECORTES}/${f}/${materia}.json`)) out.push({ q, fonte: `recorte ${f}`, materia, rank: 1 });
  for (const q of questoes(materiaPath(materia))) out.push({ q, fonte: "matéria", materia, rank: 2 });
  return out.filter((c) => formatOf(c.q) === formato && valid(c.q) && !fora(c.q));
}

// Mistura de dificuldade de uma prova: 30% fácil, 50% média, 20% difícil (posição i de n).
const alvoDificuldade = (i: number, n: number) => { const r = (i + 0.5) / n; return r < 0.3 ? "FACIL" : r < 0.8 ? "MEDIA" : "DIFICIL"; };

function montar(id: string, e: Json): { file: Json; faltam: number; total: number; fontes: Record<string, number> } {
  const banca = folderOf(String(e.banca ?? ""));
  const formato = banca === "cebraspe" ? "CERTO_ERRADO" : "MULTIPLE_CHOICE";
  const usadas = new Set<string>();
  const textos: Set<string>[] = [];
  const contaResp: Record<string, number> = {};
  let longas = 0, absolutos = 0;
  const out: Json[] = [];
  const fontes: Record<string, number> = {};
  let faltam = 0, total = 0;
  const partes: string[] = [];
  for (const d of (e.disciplinas as Json[]) ?? []) {
    const n = Number(d.questoes) || 0;
    if (n <= 0) continue;
    total += n;
    partes.push(`${d.nome} ${n}`);
    const topicos = ((d.topicos as Json[]) ?? []).filter((t) => t.topico).map((t) => ({ materia: String(t.topico), texto: String(t.texto ?? "") }));
    // Espalha as n questões pelos tópicos da disciplina, em rodízio (cada tópico do edital entra antes de repetir).
    for (let i = 0; i < n; i++) {
      const dif = alvoDificuldade(i, n);
      let escolhida: Cand | null = null;
      for (let k = 0; k < Math.max(1, topicos.length) && !escolhida; k++) {
        const t = topicos[(i + k) % Math.max(1, topicos.length)];
        if (!t) break;
        const pool = candidatos(t.materia, banca, formato).filter((c) => !usadas.has(questionKey(String(c.q.statement))) && !parecida(c.q, textos));
        if (!pool.length) continue;
        // Equilíbrio de prova (conferência de 24/11): Certo/Errado perto de meio a meio, letra da certa espalhada,
        // a certa não pode ser quase sempre a mais longa. Só escolhe entre questões prontas; nada é reescrito.
        const resp = (c: Cand) => (c.q.options as Option[]).find((o) => o.correct)!.key;
        const desequilibrio = (c: Cand) => {
          const r = resp(c), feitas = out.length || 1;
          if (formato === "CERTO_ERRADO") return (contaResp[r] ?? 0) / feitas > 0.52 ? 1 : 0;
          return ((contaResp[r] ?? 0) / feitas > 0.24 ? 1 : 0) + (certaMaisLonga(c.q) && longas / feitas > 0.3 ? 1 : 0);
        };
        const nota = (c: Cand) => c.rank * 4 + desequilibrio(c) * 2 + (absoluto(c.q) && absolutos / (out.length || 1) > 0.15 ? 2 : 0) + (c.q.difficulty === dif ? 0 : 1);
        const melhor = Math.min(...pool.map(nota));
        const faixa = pool.filter((c) => nota(c) === melhor);
        escolhida = faixa[seedOf(`${id}|1|${d.nome}|${i}`) % faixa.length];
      }
      if (!escolhida) { faltam++; continue; }
      const q = escolhida.q;
      usadas.add(questionKey(String(q.statement)));
      textos.push(palavras(String(q.statement)));
      const rk = (q.options as Option[]).find((o) => o.correct)!.key;
      contaResp[rk] = (contaResp[rk] ?? 0) + 1;
      if (formato !== "CERTO_ERRADO" && certaMaisLonga(q)) longas++;
      if (absoluto(q)) absolutos++;
      fontes[escolhida.fonte.startsWith("recorte") ? (escolhida.rank === 0 ? "banca do edital" : "outras bancas") : "matéria"] = (fontes[escolhida.fonte.startsWith("recorte") ? (escolhida.rank === 0 ? "banca do edital" : "outras bancas") : "matéria"] ?? 0) + 1;
      out.push({
        n: out.length + 1, disciplina: String(d.nome), materia: escolhida.materia, topico: q.topico ?? null, format: formato,
        difficulty: q.difficulty ?? "MEDIA", statement: q.statement,
        options: (q.options as Option[]).map((o) => ({ key: o.key, text: o.text, correct: o.correct === true })),
        explanation: q.explanation ?? "", pegadinha: null, origem: `${escolhida.fonte} · ${escolhida.materia}`,
      });
    }
  }
  const estrutura = `Simuladão montado com questões revisadas da biblioteca, na quantidade de questões de cada disciplina da prova ${String(e.concurso ?? id)} (${total} ${formato === "CERTO_ERRADO" ? "itens Certo/Errado" : "questões de múltipla escolha"}): ${partes.join(", ")}`;
  return { file: { edital: id, banca: e.banca, estrutura, numero: 1, montado: true, questions: out }, faltam, total, fontes };
}

const comSimulado = new Set([...Deno.readDirSync(SIMULADOS)].map((f) => f.name.replace(/\.simulado-\d+\.json$/, "")));
let gravados = 0;
const editais = [...Deno.readDirSync(EDITAIS)].map((f) => f.name.replace(/\.json$/, "")).filter((id) => !only || id === only).sort();
for (const id of editais) {
  if (gravados >= limite) break;
  // --refazer: remonta os Simuladões 1 que este programa montou (montado: true) e ainda não têm Simuladão 2.
  if (comSimulado.has(id)) {
    let refaz = false;
    if (args.includes("--refazer")) { try { refaz = ler(`${SIMULADOS}/${id}.simulado-1.json`).montado === true && !existsSync(`${SIMULADOS}/${id}.simulado-2.json`); } catch { /* sem */ } }
    if (!refaz) continue;
  }
  let e: Json; try { e = ler(`${EDITAIS}/${id}.json`); } catch { continue; }
  let ds = ((e.disciplinas as Json[]) ?? []).filter((d) => Number(d.questoes) > 0);
  let soma = ds.reduce((a, d) => a + Number(d.questoes), 0);
  let aproximado = false;
  if ((ds.length < 2 || soma < 20) && args.includes("--aproximado")) {
    // Sem quadro de questões no edital: tamanho típico da banca, dividido pelos tópicos de cada disciplina.
    const todas = ((e.disciplinas as Json[]) ?? []).filter((d) => ((d.topicos as Json[]) ?? []).some((t) => t.topico));
    const b = folderOf(String(e.banca ?? ""));
    const total = b === "cebraspe" ? 120 : b === "vunesp" ? 80 : ["fgv", "fcc", "cesgranrio"].includes(b ?? "") ? 60 : 50;
    const tops = todas.map((d) => ((d.topicos as Json[]) ?? []).filter((t) => t.topico).length);
    const somaT = tops.reduce((a, x) => a + x, 0);
    if (todas.length >= 2 && somaT > 0) {
      const cont = tops.map((t) => Math.max(2, Math.round((total * t) / somaT)));
      let dif = total - cont.reduce((a, x) => a + x, 0);
      for (let i = 0; dif !== 0 && i < 1000; i++) { const k = i % cont.length; if (dif > 0) { cont[k]++; dif--; } else if (cont[k] > 2) { cont[k]--; dif++; } }
      e = { ...e, disciplinas: todas.map((d, i) => ({ ...d, questoes: cont[i] })) };
      ds = e.disciplinas as Json[]; soma = total; aproximado = true;
    }
  }
  if (ds.length < 2 || soma < 20) continue;
  const { file, faltam, total, fontes } = montar(id, e);
  if (aproximado) file.estrutura = String(file.estrutura).replace("na quantidade de questões de cada disciplina da prova", "com quantidade aproximada por disciplina (o edital não traz o quadro de questões) da prova");
  const ok = faltam / total <= MAX_FALTA;
  console.log(`${id}: ${ok ? `${total - faltam}/${total}` : `NÃO gravado (faltam ${faltam} de ${total})`} · ${Object.entries(fontes).map(([k, v]) => `${k} ${v}`).join(", ")}`);
  if (!ok || dry) continue;
  Deno.writeTextFileSync(`${SIMULADOS}/${id}.simulado-1.json`, JSON.stringify(file, null, 2) + "\n");
  gravados++;
}
console.log(`\n${gravados} Simuladão 1 gravado(s).`);
