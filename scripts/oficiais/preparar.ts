// Prepara as questões de prova real extraídas (work/provas-oficiais/) para entrar no app: descarta as que
// não servem (anuladas, com figura, sem gabarito, que dependem de texto que não veio, 4 alternativas) e
// sugere a matéria de cada uma, comparando o texto da questão com as matérias publicadas (TF-IDF).
// Não escreve nem altera questão: só filtra, classifica e grava work/oficiais/questoes.jsonl.
//   deno run -A scripts/oficiais/preparar.ts
const ROOT = new URL("../../", import.meta.url).pathname.replace(/^\/([A-Za-z]:)/, "$1");
// PROVAS_OFICIAIS / SAIDA_OFICIAIS: outras pastas, para testar uma extração nova sem tocar no questoes.jsonl de verdade.
const PROVAS = Deno.env.get("PROVAS_OFICIAIS") ?? `${ROOT}work/provas-oficiais`;
const SAIDA = Deno.env.get("SAIDA_OFICIAIS") ?? `${ROOT}work/oficiais`;
type Json = Record<string, any>;

const STOP = new Set(("a o as os de da do das dos e em no na nos nas um uma uns umas por para pelo pela pelos pelas com sem que se ao aos " +
  "ou como mais menos sua seu suas seus ser sao foi esta este essa esse isso isto ele ela eles elas nao sim qual quais quando onde " +
  "julgue item itens seguir assinale alternativa correta incorreta afirmativa assertiva caso hipotese acordo considerando " +
  "texto sobre entre tambem pode deve devem cada todo toda todos todas ha ja apenas so lei art artigo").split(" "));
const words = (s: string) => s.normalize("NFD").replace(/[̀-ͯ]/g, "").toLowerCase().split(/[^a-z0-9]+/)
  .filter((w) => w.length >= 3 && !STOP.has(w) && !/^\d+$/.test(w));

// ------------------------------------------------------------ índice das matérias
// Matéria local (estado, município, órgão, estatuto ou regimento próprio) só vale para prova de lá:
// sem isso uma questão geral de LGPD caía no decreto de LGPD de Belo Horizonte.
const AREAS_LOCAIS = new Set(("acre alagoas amapa amazonas bahia ceara distrito-federal espirito-santo goias manaus maranhao mato-grosso " +
  "mato-grosso-do-sul minas-gerais para paraiba parana pernambuco piaui rio-de-janeiro rio-grande-do-norte rio-grande-do-sul rondonia " +
  "roraima santa-catarina santos sao-paulo sergipe tocantins tce-sc trt23 trt8 stj mpu caixa legislacao-estadual legislacao-municipal").split(" "));
const GENERICAS = new Set("estatuto regimento geral interno organica decreto lei servidores carreira cargos plano codigo etica conduta normas".split(" "));
const docs: { id: string; title: string; tf: Map<string, number>; local: string[] | null }[] = [];
for (const area of Deno.readDirSync(`${ROOT}conteudo/materias`)) {
  if (!area.isDirectory) continue;
  for (const f of Deno.readDirSync(`${ROOT}conteudo/materias/${area.name}`)) {
    if (!f.name.endsWith(".json")) continue;
    let m: Json; try { m = JSON.parse(Deno.readTextFileSync(`${ROOT}conteudo/materias/${area.name}/${f.name}`)); } catch { continue; }
    if (m.status && m.status !== "PUBLISHED") continue;
    const parts = [
      ...Array(3).fill(m.title ?? ""), ...(m.aliases ?? []).slice(0, 15), m.subject ?? "",
      ...(m.chapters ?? []).map((c: Json) => c.title ?? ""),
      ...(m.questions ?? []).slice(0, 80).map((q: Json) => `${q.statement ?? ""} ${(q.options ?? []).map((o: Json) => o.text).join(" ")}`),
    ];
    const tf = new Map<string, number>();
    for (const w of words(parts.join(" "))) tf.set(w, (tf.get(w) ?? 0) + 1);
    const local = AREAS_LOCAIS.has(area.name) || /(estatuto|regimento|lei-organica|pccr|plano-de-cargos)/.test(m.id)
      ? words(String(m.id).replace(/[.-]/g, " ")).filter((w) => w.length >= 4 && !GENERICAS.has(w)) : null;
    docs.push({ id: m.id, title: m.title, tf, local });
  }
}
const df = new Map<string, number>();
for (const d of docs) for (const w of d.tf.keys()) df.set(w, (df.get(w) ?? 0) + 1);
const idf = (w: string) => Math.log(docs.length / (df.get(w) ?? docs.length));
// Vetores normalizados e índice invertido palavra → [doc, peso].
const inv = new Map<string, Array<[number, number]>>();
docs.forEach((d, i) => {
  const vec = [...d.tf].map(([w, n]) => [w, (1 + Math.log(n)) * idf(w)] as const);
  const norm = Math.hypot(...vec.map(([, v]) => v)) || 1;
  for (const [w, v] of vec) { if (v <= 0) continue; (inv.get(w) ?? inv.set(w, []).get(w)!).push([i, v / norm]); }
});
function candidatos(text: string, contexto: string, k = 5): Array<{ id: string; title: string; score: number }> {
  const tf = new Map<string, number>();
  for (const w of words(text)) tf.set(w, (tf.get(w) ?? 0) + 1);
  const score = new Map<number, number>();
  let norm = 0;
  for (const [w, n] of tf) {
    const v = (1 + Math.log(n)) * idf(w);
    norm += v * v;
    for (const [i, dv] of inv.get(w) ?? []) score.set(i, (score.get(i) ?? 0) + v * dv);
  }
  norm = Math.sqrt(norm) || 1;
  const ctx = new Set(words(contexto));
  for (const [i, s] of score) { const l = docs[i].local; if (l && !l.some((w) => ctx.has(w))) score.set(i, s * 0.3); }
  return [...score].sort((a, b) => b[1] - a[1]).slice(0, k)
    .map(([i, s]) => ({ id: docs[i].id, title: docs[i].title, score: Math.round((s / norm) * 1000) / 1000 }));
}

// ------------------------------------------------------------ questões
const walk = (d: string): string[] => [...Deno.readDirSync(d)].flatMap((e) => e.isDirectory ? walk(`${d}/${e.name}`) : e.name.endsWith(".json") ? [`${d}/${e.name}`] : []);
// Questão que remete a um texto, gráfico ou figura que a extração não trouxe.
const PRECISA_TEXTO = /\b(texto|trecho|l\.\s*\d|linhas?\s+\d|par[áa]grafo|charge|tirinha|gr[áa]fico|tabela|figura|imagem|quadro|mapa|fragmento)\b/i;
const anoDe = (j: Json) => Number(j.ano) || Number(String(`${j.concurso} ${j.url} ${j.gabaritoUrl}`).match(/(?:19|20)\d\d/g)?.find((a) => +a >= 2000 && +a <= 2026)) || null;
Deno.mkdirSync(SAIDA, { recursive: true });
// Grava ao lado e troca no fim: durante os minutos de preparo o questoes.jsonl antigo continua inteiro (08/11: a
// conferência de alertas e o montar.ts liam o arquivo pela metade enquanto a coleta o refazia).
const out = await Deno.open(`${SAIDA}/questoes.jsonl.novo`, { write: true, create: true, truncate: true });
const enc = new TextEncoder();
const fora: Record<string, number> = {};
let total = 0, ok = 0, firmes = 0;
for (const banca of ["cebraspe", "fgv", "fundatec", "cesgranrio", "aocp"]) {
  for (const arquivo of walk(`${PROVAS}/${banca}`)) {
    let j: Json; try { j = JSON.parse(Deno.readTextFileSync(arquivo)); } catch { continue; }
    const ano = anoDe(j);
    for (const q of j.itens ?? j.questoes ?? []) {
      total++;
      const n = (q.alternativas ?? []).length;
      const base = String(q.base ?? "");
      const motivo = q.anulado || q.anulada ? "anulada" : q.temFigura ? "figura" : q.extracaoRuim ? "extração embaralhada" : !q.gabarito ? "sem gabarito" : !ano ? "sem ano"
        : n !== 0 && n !== 5 ? `${n} alternativas` : q.precisaTexto || (PRECISA_TEXTO.test(q.enunciado ?? "") && base.length < 400) ? "depende de texto"
        : n === 0 && !/^[CE]$/.test(q.gabarito) ? "gabarito C/E inválido" : n === 5 && !/^[A-E]$/.test(q.gabarito) ? "gabarito inválido" : null;
      if (motivo) { fora[motivo] = (fora[motivo] ?? 0) + 1; continue; }
      const texto = `${base.length < 600 ? base : ""} ${q.enunciado} ${(q.alternativas ?? []).join(" ")}`;
      const cands = candidatos(texto, `${j.nomeConcurso ?? ""} ${j.concurso} ${j.cargo ?? ""} ${j.prova ?? ""} ${j.url}`);
      // Firme: a primeira matéria se destaca bem da segunda.
      const firme = cands.length > 1 && cands[0].score >= 0.2 && cands[0].score >= cands[1].score * 1.35;
      if (firme) firmes++;
      ok++;
      await out.write(enc.encode(JSON.stringify({
        id: `${banca}:${j.concurso}:${j.arquivo}:${q.numero}`, banca: j.banca, concurso: j.nomeConcurso ?? j.concurso, cargo: j.cargo ?? j.prova ?? null,
        ano, url: j.url, numero: q.numero, base: base.length < 2000 ? base : base.slice(0, 2000), enunciado: q.enunciado,
        alternativas: q.alternativas ?? [], gabarito: q.gabarito, cands, firme,
      }) + "\n"));
    }
  }
}
out.close();
Deno.renameSync(`${SAIDA}/questoes.jsonl.novo`, `${SAIDA}/questoes.jsonl`);
console.log({ total, aproveitadas: ok, firmes, fora });
