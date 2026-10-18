// Liga tópicos de edital (topico: null) a matérias pela Message Batches API da Anthropic.
// O modelo só escolhe o id de uma matéria do catálogo (ou null); nada de conteúdo é gerado.
//
// Uso:
//   node scripts/biblioteca/ligar-api.mjs enviar <pasta-editais> [--limite N] [--por 250]
//   node scripts/biblioteca/ligar-api.mjs status
//   node scripts/biblioteca/ligar-api.mjs baixar
//   node scripts/biblioteca/ligar-api.mjs aplicar <pasta-editais> [--com-media]
//
// Resultado em conteudo/entrada/ligacoes-api/ (respostas por pedido + ligacoes.json consolidado).

import fs from "node:fs";
import path from "node:path";

const ROOT = path.resolve(path.dirname(new URL(import.meta.url).pathname.replace(/^\/(\w:)/, "$1")), "../..");
const SAIDA = path.join(ROOT, "conteudo/entrada/ligacoes-api");
const ESTADO = path.join(SAIDA, "_batches.json");
const MODELO = process.env.LIGAR_MODELO || "claude-sonnet-5-5";

function env(nome) {
  const t = fs.readFileSync(path.join(ROOT, ".env.anthropic"), "utf8");
  const m = t.match(new RegExp(`${nome}=(\\S+)`));
  return m ? m[1] : null;
}
async function api(metodo, rota, corpo) {
  const ws = env("ANTHROPIC_WORKSPACE_ID");
  const r = await fetch("https://api.anthropic.com/v1/" + rota, {
    method: metodo,
    headers: { "x-api-key": env("ANTHROPIC_API_KEY"), "anthropic-version": "2023-06-01", "content-type": "application/json", ...(ws ? { "anthropic-workspace-id": ws } : {}) },
    body: corpo ? JSON.stringify(corpo) : undefined,
  });
  const txt = await r.text();
  if (!r.ok) throw new Error(`${metodo} ${rota}: ${r.status} ${txt.slice(0, 500)}`);
  return txt;
}

function catalogo() {
  const linhas = [];
  for (const area of fs.readdirSync(path.join(ROOT, "conteudo/materias"))) {
    const dir = path.join(ROOT, "conteudo/materias", area);
    if (!fs.statSync(dir).isDirectory()) continue;
    for (const f of fs.readdirSync(dir).filter((x) => x.endsWith(".json"))) {
      const m = JSON.parse(fs.readFileSync(path.join(dir, f), "utf8"));
      const escopo = String(m.scope?.covers ?? "").replace(/\s+/g, " ").slice(0, 140);
      linhas.push(`${m.id} | ${m.title}${escopo ? " — " + escopo : ""}`);
    }
  }
  return linhas.sort().join("\n");
}

function pendentes(pasta) {
  const mapa = new Map();
  for (const f of fs.readdirSync(pasta).filter((x) => x.endsWith(".json"))) {
    const ed = JSON.parse(fs.readFileSync(path.join(pasta, f), "utf8"));
    for (const d of ed.disciplinas) for (const t of d.topicos) {
      if (t.topico) continue;
      const k = `${d.nome}\u0001${t.texto}`;
      if (!mapa.has(k)) mapa.set(k, { disciplina: d.nome, texto: t.texto, cargo: ed.cargoExibicao || ed.cargo });
    }
  }
  return [...mapa.values()].sort((a, b) => a.disciplina.localeCompare(b.disciplina));
}

const SISTEMA = (cat) => `Você liga tópicos de editais de concursos públicos às matérias de uma biblioteca de estudo.
Para cada tópico, escolha a ÚNICA matéria do catálogo que realmente ensina aquele conteúdo, ou null se nenhuma ensina.

Regras:
- Use o id exatamente como está no catálogo. Nunca invente id.
- Considere a disciplina: "Recursos" em Direito Processual Civil é diferente de "Recursos" em Administração.
- Confiança "A" (alta): a matéria é exatamente sobre aquele assunto. "M" (média): a matéria cobre o assunto, mas não é o foco. Se for só tangencial, use null.
- null quando: o tópico é pedaço solto sem assunto ("Conceito.", "Seção II."), norma própria de órgão (regimento interno, lei orgânica municipal, estatuto estadual específico) sem matéria correspondente, cargo técnico sem matéria (engenharia, medicina etc.), ou tópico que junta várias matérias diferentes.
- Lei estadual ou municipal só liga a matéria do MESMO estado/município.
- Responda SOMENTE com JSON: {"r":[{"i":<número do tópico>,"m":"<id>"|null,"c":"A"|"M"}]} com uma entrada para cada tópico, na mesma ordem.

CATÁLOGO (id | título — escopo):
${cat}`;

function lerEstado() { try { return JSON.parse(fs.readFileSync(ESTADO, "utf8")); } catch { return []; } }
function salvarEstado(e) { fs.mkdirSync(SAIDA, { recursive: true }); fs.writeFileSync(ESTADO, JSON.stringify(e, null, 2)); }
const arg = (nome, padrao) => { const i = process.argv.indexOf(nome); return i > 0 ? process.argv[i + 1] : padrao; };

const [cmd, pasta] = process.argv.slice(2);

if (cmd === "enviar") {
  const todos = pendentes(pasta);
  const limite = Number(arg("--limite", todos.length));
  const por = Number(arg("--por", 250));
  const inicio = Number(arg("--inicio", 0));
  const itens = todos.slice(inicio, inicio + limite);
  const sistema = SISTEMA(catalogo());
  const reqs = [];
  fs.mkdirSync(SAIDA, { recursive: true });
  for (let p = 0; p < itens.length; p += por) {
    const parte = itens.slice(p, p + por);
    const id = `lig-${Date.now().toString(36)}-${String(p / por).padStart(4, "0")}`;
    fs.writeFileSync(path.join(SAIDA, `${id}.pedido.json`), JSON.stringify(parte));
    const corpo = parte.map((t, i) => `${i + 1}. [${t.disciplina}] ${t.texto}`).join("\n");
    reqs.push({
      custom_id: id,
      params: {
        model: MODELO, max_tokens: 16000,
        thinking: { type: "between_tools" },
        system: [{ type: "text", text: sistema, cache_control: { type: "ephemeral" } }],
        messages: [{ role: "user", content: `Tópicos:\n${corpo}` }],
      },
    });
  }
  const r = JSON.parse(await api("POST", "messages/batches", { requests: reqs }));
  const e = lerEstado();
  e.push({ id: r.id, criado: new Date().toISOString(), modelo: MODELO, topicos: itens.length, pedidos: reqs.length, baixado: false });
  salvarEstado(e);
  console.log("ligação enviada:", r.id, "| tópicos:", itens.length, "| pedidos:", reqs.length, "| catálogo:", sistema.length, "caracteres");
} else if (cmd === "status") {
  for (const b of lerEstado().filter((x) => !x.baixado)) {
    const r = JSON.parse(await api("GET", "messages/batches/" + b.id));
    console.log(b.id, r.processing_status, JSON.stringify(r.request_counts));
  }
} else if (cmd === "baixar") {
  const ids = new Set();
  for (const area of fs.readdirSync(path.join(ROOT, "conteudo/materias"))) {
    const dir = path.join(ROOT, "conteudo/materias", area);
    if (fs.statSync(dir).isDirectory()) for (const f of fs.readdirSync(dir).filter((x) => x.endsWith(".json"))) ids.add(JSON.parse(fs.readFileSync(path.join(dir, f), "utf8")).id);
  }
  const consolidado = path.join(SAIDA, "ligacoes.json");
  const todas = fs.existsSync(consolidado) ? JSON.parse(fs.readFileSync(consolidado, "utf8")) : [];
  const e = lerEstado();
  for (const b of e.filter((x) => !x.baixado)) {
    const st = JSON.parse(await api("GET", "messages/batches/" + b.id));
    if (st.processing_status !== "ended") { console.log(b.id, "ainda", st.processing_status); continue; }
    const linhas = (await api("GET", `messages/batches/${b.id}/results`)).trim().split("\n");
    const u = { entrada: 0, cache: 0, saida: 0 };
    let a = 0, m = 0, n = 0, inval = 0;
    for (const ln of linhas) {
      const r = JSON.parse(ln);
      if (r.result.type !== "succeeded") { console.log(r.custom_id, "FALHOU"); continue; }
      const msg = r.result.message;
      u.entrada += msg.usage.input_tokens; u.cache += msg.usage.cache_read_input_tokens ?? 0; u.saida += msg.usage.output_tokens;
      const parte = JSON.parse(fs.readFileSync(path.join(SAIDA, `${r.custom_id}.pedido.json`), "utf8"));
      let txt = msg.content.filter((c) => c.type === "text").map((c) => c.text).join("").trim().replace(/^```(?:json)?\s*/, "").replace(/```\s*$/, "");
      let resp;
      try { resp = JSON.parse(txt).r; } catch { console.log(r.custom_id, "resposta inválida"); continue; }
      for (const x of resp) {
        const t = parte[x.i - 1];
        if (!t) continue;
        if (x.m && !ids.has(x.m)) { inval++; continue; }
        todas.push({ disciplina: t.disciplina, texto: t.texto, materia: x.m ?? null, confianca: x.m ? x.c : null });
        if (!x.m) n++; else if (x.c === "A") a++; else m++;
      }
    }
    b.baixado = true; b.uso = u;
    console.log(b.id, `| alta ${a} | média ${m} | sem matéria ${n} | id inválido ${inval} | tokens entrada ${u.entrada} cache ${u.cache} saída ${u.saida}`);
  }
  fs.writeFileSync(consolidado, JSON.stringify(todas, null, 1));
  salvarEstado(e);
} else if (cmd === "aplicar") {
  const comMedia = process.argv.includes("--com-media");
  const todas = JSON.parse(fs.readFileSync(path.join(SAIDA, "ligacoes.json"), "utf8"));
  const mapa = new Map(todas.filter((x) => x.materia && (x.confianca === "A" || comMedia)).map((x) => [`${x.disciplina}\u0001${x.texto}`, x.materia]));
  let total = 0;
  for (const f of fs.readdirSync(pasta).filter((x) => x.endsWith(".json"))) {
    const p = path.join(pasta, f);
    const ed = JSON.parse(fs.readFileSync(p, "utf8"));
    let n = 0;
    for (const d of ed.disciplinas) for (const t of d.topicos) {
      if (t.topico) continue;
      const id = mapa.get(`${d.nome}\u0001${t.texto}`);
      if (id) { t.topico = id; n++; }
    }
    if (n) { fs.writeFileSync(p, JSON.stringify(ed, null, 2) + "\n"); total += n; }
  }
  console.log("tópicos ligados:", total);
} else {
  console.log("uso: enviar <pasta> [--limite N] [--por 250] | status | baixar | aplicar <pasta> [--com-media]");
}
