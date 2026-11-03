// Junta as respostas do ChatGPT (conteudo/entrada/oficiais/respostas/oficial-*.json: matéria escolhida e
// explicação) às questões de prova real (work/oficiais/questoes.jsonl) e grava conteudo/recortes-oficiais/
// <banca>/<matéria>.json no formato dos recortes, com sourceType "REAL", link, banca, órgão e ano.
// Só converte: enunciado, alternativas e gabarito são os da prova. Fica de fora a questão sem matéria
// válida, com explicação curta ou com alerta (gabarito suspeito ou desatualizada), que vai para
// work/oficiais/alertas.jsonl. O importar.ts junta estes arquivos ao recorte da mesma banca.
//   deno run -A scripts/oficiais/montar.ts
const ROOT = new URL("../../", import.meta.url).pathname.replace(/^\/([A-Za-z]:)/, "$1");
const RESPOSTAS = `${ROOT}conteudo/entrada/oficiais/respostas`;
const SAIDA = `${ROOT}conteudo/recortes-oficiais`;
type Json = Record<string, any>;

const questoes = new Map<string, Json>();
for (const l of Deno.readTextFileSync(`${ROOT}work/oficiais/questoes.jsonl`).trim().split("\n")) { const q = JSON.parse(l); questoes.set(q.id, q); }
const materias = new Map<string, Json>();
for (const area of Deno.readDirSync(`${ROOT}conteudo/materias`)) {
  if (!area.isDirectory) continue;
  for (const f of Deno.readDirSync(`${ROOT}conteudo/materias/${area.name}`)) {
    if (!f.name.endsWith(".json")) continue;
    try { const m = JSON.parse(Deno.readTextFileSync(`${ROOT}conteudo/materias/${area.name}/${f.name}`)); materias.set(m.id, m); } catch { /* ignora */ }
  }
}
const norm = (s: string) => s.normalize("NFD").replace(/[̀-ͯ]/g, "").toLowerCase();
const palavras = (s: string) => new Set(norm(s).split(/[^a-z0-9]+/).filter((w) => w.length >= 4));
// Capítulo da matéria com mais palavras em comum com a questão (a seção tem de ser um capítulo dela).
function secao(m: Json, texto: string): string {
  const q = palavras(texto);
  let melhor = m.chapters[0], pontos = -1;
  for (const c of m.chapters) {
    const p = [...palavras(`${c.title} ${String(c.markdown ?? "").slice(0, 3000)}`)].filter((w) => q.has(w)).length;
    if (p > pontos) { melhor = c; pontos = p; }
  }
  return String(melhor.title).trim();
}
const slug = (banca: string) => norm(banca).replace(/[^a-z0-9]+/g, "-").replace(/^-|-$/g, "");

const porRecorte = new Map<string, { banca: string; topic: string; questions: Json[] }>();
const alertas: Json[] = [];
const contagem = { respostas: 0, aceitas: 0, semMateria: 0, curtas: 0, alerta: 0, desconhecidas: 0 };
let arquivos: string[] = [];
try { arquivos = [...Deno.readDirSync(RESPOSTAS)].filter((e) => e.name.endsWith(".json")).map((e) => e.name).sort(); } catch { /* nenhuma ainda */ }
for (const arquivo of arquivos) {
  let r: Json; try { r = JSON.parse(Deno.readTextFileSync(`${RESPOSTAS}/${arquivo}`)); } catch { continue; }
  for (const it of r.itens ?? []) {
    contagem.respostas++;
    const q = questoes.get(String(it.id));
    if (!q) { contagem.desconhecidas++; continue; }
    const escolhida = String(it.materia ?? "").trim().split(/\s/)[0];
    const m = materias.get(escolhida);
    if (!m || !q.cands.some((c: Json) => c.id === escolhida)) { contagem.semMateria++; continue; }
    const explicacao = String(it.explicacao ?? "").trim();
    if (String(it.alerta ?? "").trim()) { contagem.alerta++; alertas.push({ id: q.id, alerta: it.alerta, explicacao }); continue; }
    if (explicacao.length < 120) { contagem.curtas++; continue; }
    const ce = q.alternativas.length === 0;
    const statement = `${q.base && q.base.length > 20 && !/^\(?Dispon[ií]vel em/i.test(q.base) ? `${q.base.trim()}\n\n` : ""}${String(q.enunciado).trim()}`;
    const questao: Json = {
      topico: null, estilo: String(q.banca).toUpperCase(), statement,
      format: ce ? "TRUE_FALSE" : "MULTIPLE_CHOICE", difficulty: "MEDIA",
      options: ce
        ? [{ key: "C", text: "Certo", correct: q.gabarito === "C" }, { key: "E", text: "Errado", correct: q.gabarito === "E" }]
        : q.alternativas.map((t: string, k: number) => ({ key: "ABCDE"[k], text: String(t).trim(), correct: "ABCDE"[k] === q.gabarito })),
      explanation: explicacao, section: secao(m, statement),
      errorConceptKey: m.errorConcepts?.[0]?.key ?? null,
      sourceType: "REAL", sourceUrl: q.url, board: q.banca, agency: q.concurso, year: q.ano,
    };
    const chave = `${slug(q.banca)}/${escolhida}`;
    const grupo = porRecorte.get(chave) ?? { banca: q.banca, topic: escolhida, questions: [] as Json[] };
    grupo.questions.push(questao);
    porRecorte.set(chave, grupo);
    contagem.aceitas++;
  }
}
try { Deno.removeSync(SAIDA, { recursive: true }); } catch { /* primeira vez */ }
for (const [chave, g] of porRecorte) {
  Deno.mkdirSync(`${SAIDA}/${chave.split("/")[0]}`, { recursive: true });
  Deno.writeTextFileSync(`${SAIDA}/${chave}.json`, JSON.stringify({ topic: g.topic, board: g.banca, role: null, questions: g.questions }, null, 1) + "\n");
}
Deno.writeTextFileSync(`${ROOT}work/oficiais/alertas.jsonl`, alertas.map((a) => JSON.stringify(a)).join("\n") + (alertas.length ? "\n" : ""));
console.log({ ...contagem, recortes: porRecorte.size });
