// Simulados prontos por edital (conteudo/simulados/<edital>.simulado-N.json).
//
//   node scripts/biblioteca/simulados.mjs ligar      liga cada questão à matéria (pelo tópico do edital;
//                                                    o que sobrar vai para conteudo/entrada/simulados-pendentes.json)
//   node scripts/biblioteca/simulados.mjs aplicar <ligacoes.json>   grava ligações vindas da API
//   node scripts/biblioteca/simulados.mjs planta     grava no edital a estrutura da prova (questões por
//                                                    disciplina, formato, regra) e a marca de simulado pronto
//   node scripts/biblioteca/simulados.mjs banco      copia as questões para os recortes da banca (banco único:
//                                                    servem a qualquer edital com a mesma matéria e banca)
//
// Só transforma e liga: nenhuma questão é escrita ou alterada no conteúdo.

import fs from "node:fs";
import path from "node:path";

const ROOT = path.resolve(path.dirname(new URL(import.meta.url).pathname.replace(/^\/(\w:)/, "$1")), "../..");
const SIM = path.join(ROOT, "conteudo/simulados");
const ED = path.join(ROOT, "conteudo/editais");
const REC = path.join(ROOT, "conteudo/recortes");

const norm = (s) => String(s ?? "").normalize("NFD").replace(/[̀-ͯ]/g, "").toLowerCase().replace(/[^a-z0-9 ]/g, " ").replace(/\s+/g, " ").trim();
const STOP = new Set("de da do das dos e a o em no na nos nas para por com sua seu as os ao aos lei n que se um uma".split(" "));
const palavras = (s) => norm(s).split(" ").filter((w) => w.length > 2 && !STOP.has(w));

function materias() {
  const m = new Map();
  for (const area of fs.readdirSync(path.join(ROOT, "conteudo/materias"))) {
    const dir = path.join(ROOT, "conteudo/materias", area);
    if (!fs.statSync(dir).isDirectory()) continue;
    for (const f of fs.readdirSync(dir).filter((x) => x.endsWith(".json"))) {
      const j = JSON.parse(fs.readFileSync(path.join(dir, f), "utf8"));
      m.set(j.id, j);
    }
  }
  return m;
}
const simulados = () => fs.readdirSync(SIM).filter((f) => /\.simulado-\d+\.json$/.test(f)).map((f) => ({ f, p: path.join(SIM, f), j: JSON.parse(fs.readFileSync(path.join(SIM, f), "utf8")) }));
function editalDe(sim) {
  const p = path.join(ED, `${sim.j.edital}.json`);
  if (fs.existsSync(p)) return { p, j: JSON.parse(fs.readFileSync(p, "utf8")) };
  // nome antigo: procura o edital com o mesmo começo (ex.: prf-2021 → prf-policial-rodoviario-federal-2021)
  const [org, ano] = [sim.j.edital.split("-")[0], (sim.j.edital.match(/\d{4}/) ?? [""])[0]];
  const alt = fs.readdirSync(ED).find((f) => f.startsWith(org + "-") && f.includes(ano));
  return alt ? { p: path.join(ED, alt), j: JSON.parse(fs.readFileSync(path.join(ED, alt), "utf8")) } : null;
}

const [cmd, arg] = process.argv.slice(2);

if (cmd === "ligar") {
  const ids = materias();
  const pend = new Map();
  let direto = 0, peloEdital = 0, falta = 0;
  for (const s of simulados()) {
    const ed = editalDe(s);
    if (ed && ed.j && s.j.edital !== path.basename(ed.p, ".json")) s.j.edital = path.basename(ed.p, ".json");
    const topicos = ed ? ed.j.disciplinas.flatMap((d) => d.topicos.filter((t) => t.topico).map((t) => ({ d: norm(d.nome), w: new Set(palavras(t.texto)), id: t.topico }))) : [];
    for (const q of s.j.questions) {
      if (ids.has(q.materia)) { direto++; continue; }
      // tópico do edital mais parecido (palavras em comum), na mesma disciplina se possível
      const w = palavras(`${q.topico ?? ""} ${q.disciplina ?? ""}`);
      let best = null, bs = 0;
      for (const t of topicos) {
        const s2 = w.filter((x) => t.w.has(x)).length / Math.max(3, Math.min(w.length, t.w.size)) + (t.d === norm(q.disciplina) ? 0.15 : 0);
        if (s2 > bs) { bs = s2; best = t; }
      }
      if (best && bs >= 0.6) { q.materia = best.id; peloEdital++; continue; }
      q.materia = null;
      falta++;
      const k = `${q.disciplina}\u0001${q.topico}`;
      if (!pend.has(k)) pend.set(k, { disciplina: q.disciplina, texto: q.topico || q.statement.slice(0, 200), atual: null });
    }
    fs.writeFileSync(s.p, JSON.stringify(s.j, null, 2) + "\n");
  }
  fs.writeFileSync(path.join(ROOT, "conteudo/entrada/simulados-pendentes.json"), JSON.stringify([...pend.values()], null, 1));
  console.log(`já certas: ${direto} | ligadas pelo edital: ${peloEdital} | sem matéria: ${falta} (${pend.size} tópicos para a API)`);
} else if (cmd === "aplicar") {
  const lig = JSON.parse(fs.readFileSync(arg, "utf8"));
  const mapa = new Map(lig.filter((x) => x.ids?.length).map((x) => [`${x.disciplina}\u0001${x.texto}`, x.ids[0]]));
  let n = 0;
  for (const s of simulados()) {
    for (const q of s.j.questions) if (!q.materia) { const id = mapa.get(`${q.disciplina}\u0001${q.topico}`); if (id) { q.materia = id; n++; } }
    fs.writeFileSync(s.p, JSON.stringify(s.j, null, 2) + "\n");
  }
  console.log("ligadas pela API:", n);
} else if (cmd === "planta") {
  let n = 0;
  for (const s of simulados()) {
    const ed = editalDe(s);
    if (!ed) { console.log("sem edital:", s.f); continue; }
    const cont = new Map();
    for (const q of s.j.questions) cont.set(norm(q.disciplina), (cont.get(norm(q.disciplina)) ?? 0) + 1);
    // questões por disciplina do edital = itens da prova real (casa pelo nome da disciplina)
    for (const d of ed.j.disciplinas) {
      const k = [...cont.keys()].find((c) => c === norm(d.nome) || norm(d.nome).includes(c) || c.includes(norm(d.nome)));
      if (k && !Number.isInteger(d.questoes)) d.questoes = cont.get(k);
    }
    const formato = s.j.questions.every((q) => q.format === "CERTO_ERRADO") ? "CERTO_ERRADO" : s.j.questions.every((q) => q.format === "MULTIPLE_CHOICE") ? "MULTIPLA_ESCOLHA" : "MISTO";
    ed.j.prova = { formato, itens: s.j.questions.length, estrutura: s.j.estrutura ?? null, simulados: [path.basename(s.f, ".json")] };
    fs.writeFileSync(ed.p, JSON.stringify(ed.j, null, 2) + "\n");
    n++;
  }
  console.log("editais com planta da prova:", n);
} else if (cmd === "banco") {
  const mats = materias();
  const dirBanca = (b) => {
    const d = norm(b).replace(/ /g, "-");
    return fs.existsSync(path.join(REC, d)) ? d : null;
  };
  const total = {};
  for (const s of simulados()) {
    const banca = dirBanca(s.j.banca) ?? "geral";
    for (const q of s.j.questions) {
      const m = mats.get(q.materia);
      if (!m) continue;
      // O banco exige explicação completa (o aluno precisa entender o porquê); as curtas ficam só no simulado.
      if ((q.explanation ?? "").length + (q.pegadinha ? q.pegadinha.length + 12 : 0) < 120) continue;
      const alvo = fs.existsSync(path.join(REC, banca, `${m.id}.json`)) ? banca : fs.existsSync(path.join(REC, "geral", `${m.id}.json`)) ? "geral" : null;
      // Só entra em recorte que já existe (recorte novo precisa de incidência e "como cai" escritos à mão).
      if (!alvo) continue;
      const p = path.join(REC, alvo, `${m.id}.json`);
      const r = fs.existsSync(p) ? JSON.parse(fs.readFileSync(p, "utf8")) : { topic: m.id, board: s.j.banca, role: null, version: 1, incidence: null, howItFalls: null, tips: [], traps: [], questions: [] };
      if (r.questions.some((x) => x.statement === q.statement)) continue;
      // capítulo da matéria pelo vocabulário
      const w = new Set(palavras(`${q.topico ?? ""} ${q.statement}`));
      let sec = m.chapters[0].title, bs = -1;
      for (const c of m.chapters) { const sc = palavras(c.title + " " + c.markdown.slice(0, 3000)).filter((x) => w.has(x)).length; if (sc > bs) { bs = sc; sec = c.title; } }
      const ce = q.format === "CERTO_ERRADO";
      const texto = q.textoBase ? `${q.textoBase}\n\n${q.statement}` : q.statement;
      if (texto.length > 4000 || texto.trim().length < 20) continue;
      r.questions.push({
        topico: q.topico ?? null, estilo: String(s.j.banca ?? "").toUpperCase(), statement: texto,
        format: ce ? "TRUE_FALSE" : "MULTIPLE_CHOICE",
        difficulty: ["FACIL", "MEDIA", "DIFICIL"].includes(q.difficulty) ? q.difficulty : "MEDIA",
        options: ce ? [{ key: "C", text: "Certo", correct: q.options.find((o) => o.correct)?.key === "C" }, { key: "E", text: "Errado", correct: q.options.find((o) => o.correct)?.key === "E" }]
          : q.options.map((o) => ({ key: o.key, text: o.text, correct: !!o.correct, comentario: o.comentario ?? "" })),
        explanation: q.explanation + (q.pegadinha ? ` Pegadinha: ${q.pegadinha}` : ""),
        section: sec, errorConceptKey: null, sourceType: "AUTHORIAL", sourceUrl: null, board: null, agency: null, year: null,
        simulado: path.basename(s.f, ".json"),
      });
      r.version = (r.version ?? 1) + 1;
      fs.mkdirSync(path.dirname(p), { recursive: true });
      fs.writeFileSync(p, JSON.stringify(r, null, 2) + "\n");
      total[banca] = (total[banca] ?? 0) + 1;
    }
  }
  console.log("questões levadas ao banco, por banca:", JSON.stringify(total));
} else {
  console.log("uso: ligar | aplicar <ligacoes.json> | planta | banco");
}
