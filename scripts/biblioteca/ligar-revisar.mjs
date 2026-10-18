// Revisa ligações tópico → matéria já feitas, pela Message Batches API: a matéria cobre o tópico
// inteiro? Precisa de outras para completar? Está errada? O modelo só escolhe ids do catálogo.
//
// Uso:
//   node scripts/biblioteca/ligar-revisar.mjs enviar <lista.json> [--por 150]   (lista: [{disciplina,texto,atual}])
//   node scripts/biblioteca/ligar-revisar.mjs status
//   node scripts/biblioteca/ligar-revisar.mjs baixar
//   node scripts/biblioteca/ligar-revisar.mjs aplicar            (grava editais e conteudo/pedidos/rotas.json)
//
// Resultado em conteudo/entrada/ligacoes-api/revisao/.

import fs from "node:fs";
import path from "node:path";

const ROOT = path.resolve(path.dirname(new URL(import.meta.url).pathname.replace(/^\/(\w:)/, "$1")), "../..");
const SAIDA = path.join(ROOT, "conteudo/entrada/ligacoes-api/revisao");
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
function materias() {
  const lista = [];
  for (const area of fs.readdirSync(path.join(ROOT, "conteudo/materias"))) {
    const dir = path.join(ROOT, "conteudo/materias", area);
    if (!fs.statSync(dir).isDirectory()) continue;
    for (const f of fs.readdirSync(dir).filter((x) => x.endsWith(".json"))) {
      const m = JSON.parse(fs.readFileSync(path.join(dir, f), "utf8"));
      lista.push({ id: m.id, title: m.title, escopo: String(m.scope?.covers ?? "").replace(/\s+/g, " ").slice(0, 160), fora: String(m.scope?.excludes ?? "").replace(/\s+/g, " ").slice(0, 100) });
    }
  }
  return lista.sort((a, b) => a.id.localeCompare(b.id));
}

const SISTEMA = (cat) => `Você revisa a ligação entre tópicos de editais de concurso e as matérias de uma biblioteca de estudo.
O aluno vai estudar EXATAMENTE as matérias ligadas ao tópico, confiando que elas cobrem tudo o que o edital pede ali. Seja rigoroso.

Para cada tópico você recebe a disciplina, o texto do edital e a matéria ligada hoje. Responda:
- "ids": a lista MÍNIMA de matérias do catálogo que, juntas, ensinam o tópico inteiro (até 6, a principal primeiro). Mantenha a atual se ela serve; troque se estiver errada; acrescente as que faltam para cobrir os outros assuntos do tópico.
- "cobre": true se essas matérias, juntas, cobrem TODO o conteúdo do tópico; false se sobra parte que nenhuma matéria do catálogo ensina.
- "ids": [] se nenhuma matéria ensina o tópico de verdade (ex.: lei estadual, municipal ou regimento de órgão sem matéria própria; matéria apenas tangencial; pedaço solto sem assunto).

Regras:
- Use só ids que estão no catálogo, exatamente como escritos. Nunca invente.
- Lei estadual/municipal, estatuto de servidores de um estado, constituição estadual, regimento interno: só ligue a matéria específica DAQUELE ente. Matéria federal genérica (ex.: Lei 8.112) NÃO serve para estatuto estadual: nesse caso ids [] (ou a matéria específica do estado, se existir).
- Não ligue matéria muito mais ampla só porque menciona o assunto de passagem; ela precisa ensinar o assunto.
- Considere a disciplina para desambiguar ("Recursos" em processo civil ≠ em administração).
- Responda SOMENTE com JSON: {"r":[{"i":<n>,"ids":["id",...],"cobre":true|false}]} — uma entrada por tópico, na ordem.

CATÁLOGO (id | título — o que cobre || o que não cobre):
${cat}`;

function lerEstado() { try { return JSON.parse(fs.readFileSync(ESTADO, "utf8")); } catch { return []; } }
function salvarEstado(e) { fs.mkdirSync(SAIDA, { recursive: true }); fs.writeFileSync(ESTADO, JSON.stringify(e, null, 2)); }
const arg = (nome, padrao) => { const i = process.argv.indexOf(nome); return i > 0 ? process.argv[i + 1] : padrao; };
const [cmd, lista] = process.argv.slice(2);

if (cmd === "enviar") {
  const itens = JSON.parse(fs.readFileSync(lista, "utf8"));
  const por = Number(arg("--por", 150));
  const cat = materias().map((m) => `${m.id} | ${m.title}${m.escopo ? " — " + m.escopo : ""}${m.fora ? " || " + m.fora : ""}`).join("\n");
  const sistema = SISTEMA(cat);
  fs.mkdirSync(SAIDA, { recursive: true });
  const reqs = [];
  const base = Date.now().toString(36);
  for (let p = 0; p < itens.length; p += por) {
    const parte = itens.slice(p, p + por);
    const id = `rev-${base}-${String(p / por).padStart(4, "0")}`;
    fs.writeFileSync(path.join(SAIDA, `${id}.pedido.json`), JSON.stringify(parte));
    const corpo = parte.map((t, i) => `${i + 1}. [${t.disciplina}] ${t.texto}\n   ligada hoje: ${t.atual}`).join("\n");
    reqs.push({ custom_id: id, params: { model: MODELO, max_tokens: 16000, thinking: { type: "between_tools" }, system: [{ type: "text", text: sistema, cache_control: { type: "ephemeral" } }], messages: [{ role: "user", content: `Tópicos:\n${corpo}` }] } });
  }
  const r = JSON.parse(await api("POST", "messages/batches", { requests: reqs }));
  const e = lerEstado();
  e.push({ id: r.id, criado: new Date().toISOString(), modelo: MODELO, topicos: itens.length, pedidos: reqs.length, baixado: false });
  salvarEstado(e);
  console.log("revisão enviada:", r.id, "| tópicos:", itens.length, "| pedidos:", reqs.length);
} else if (cmd === "status") {
  for (const b of lerEstado().filter((x) => !x.baixado)) {
    const r = JSON.parse(await api("GET", "messages/batches/" + b.id));
    console.log(b.id, r.processing_status, JSON.stringify(r.request_counts));
  }
} else if (cmd === "baixar") {
  const ids = new Set(materias().map((m) => m.id));
  const arq = path.join(SAIDA, "revisao.json");
  const todas = fs.existsSync(arq) ? JSON.parse(fs.readFileSync(arq, "utf8")) : [];
  const e = lerEstado();
  for (const b of e.filter((x) => !x.baixado)) {
    const st = JSON.parse(await api("GET", "messages/batches/" + b.id));
    if (st.processing_status !== "ended") { console.log(b.id, "ainda", st.processing_status); continue; }
    const linhas = (await api("GET", `messages/batches/${b.id}/results`)).trim().split("\n");
    const u = { entrada: 0, cache: 0, saida: 0 };
    const cont = { mantida: 0, trocada: 0, ampliada: 0, removida: 0, parcial: 0, invalida: 0 };
    for (const ln of linhas) {
      const r = JSON.parse(ln);
      if (r.result.type !== "succeeded") { console.log(r.custom_id, "FALHOU"); continue; }
      const msg = r.result.message;
      u.entrada += msg.usage.input_tokens; u.cache += msg.usage.cache_read_input_tokens ?? 0; u.saida += msg.usage.output_tokens;
      const parte = JSON.parse(fs.readFileSync(path.join(SAIDA, `${r.custom_id}.pedido.json`), "utf8"));
      const txt = msg.content.filter((c) => c.type === "text").map((c) => c.text).join("").trim().replace(/^```(?:json)?\s*/, "").replace(/```\s*$/, "");
      let resp;
      try { resp = JSON.parse(txt).r; } catch { console.log(r.custom_id, "resposta inválida"); continue; }
      for (const x of resp) {
        const t = parte[x.i - 1];
        if (!t || !Array.isArray(x.ids)) continue;
        const validos = [...new Set(x.ids.filter((id) => ids.has(id)))].slice(0, 6);
        if (validos.length !== x.ids.length) cont.invalida++;
        todas.push({ disciplina: t.disciplina, texto: t.texto, antes: t.atual, ids: validos, cobre: x.cobre !== false });
        if (validos.length === 0) cont.removida++;
        else if (validos.length > 1) cont.ampliada++;
        else if (validos[0] === t.atual) cont.mantida++;
        else cont.trocada++;
        if (validos.length && x.cobre === false) cont.parcial++;
      }
    }
    b.baixado = true; b.uso = u;
    console.log(b.id, JSON.stringify(cont), "| tokens entrada", u.entrada, "cache", u.cache, "saída", u.saida);
  }
  fs.writeFileSync(arq, JSON.stringify(todas, null, 1));
  salvarEstado(e);
} else if (cmd === "aplicar") {
  const todas = JSON.parse(fs.readFileSync(path.join(SAIDA, "revisao.json"), "utf8"));
  const mapa = new Map(todas.map((x) => [`${x.disciplina}\u0001${x.texto}`, x]));
  let mudou = 0, removidos = 0;
  for (const f of fs.readdirSync(path.join(ROOT, "conteudo/editais")).filter((x) => x.endsWith(".json"))) {
    const p = path.join(ROOT, "conteudo/editais", f);
    const ed = JSON.parse(fs.readFileSync(p, "utf8"));
    let n = 0;
    for (const d of ed.disciplinas) for (const t of d.topicos) {
      const x = mapa.get(`${d.nome}\u0001${t.texto}`);
      if (!x || t.topico !== x.antes) continue;
      // Ligação direta só quando UMA matéria cobre o tópico inteiro. Com várias (ou cobertura parcial),
      // o tópico fica sem ligação e a rota (rotas.json) monta o material com todas, ou passa à IA.
      const novo = x.ids.length === 1 && x.cobre ? x.ids[0] : null;
      if (novo !== t.topico) { t.topico = novo; n++; if (!novo && x.ids.length === 0) removidos++; }
    }
    if (n) { fs.writeFileSync(p, JSON.stringify(ed, null, 2) + "\n"); mudou += n; }
  }
  // Rotas: tópico que precisa de mais de uma matéria, ou que as matérias cobrem só em parte.
  const rotasPath = path.join(ROOT, "conteudo/pedidos/rotas.json");
  const rotas = JSON.parse(fs.readFileSync(rotasPath, "utf8"));
  const porTexto = new Map(rotas.map((r) => [r.texto, r]));
  let novas = 0;
  for (const x of todas) {
    if (x.ids.length === 0) continue;
    if (x.ids.length === 1 && x.cobre) continue;
    const rota = { texto: x.texto, ids: x.ids, ...(x.cobre ? {} : { cobertura: "PARTIAL" }) };
    if (!porTexto.has(x.texto)) novas++;
    porTexto.set(x.texto, rota);
  }
  fs.writeFileSync(rotasPath, JSON.stringify([...porTexto.values()], null, 1) + "\n");
  console.log("tópicos alterados:", mudou, "| sem matéria agora:", removidos, "| rotas novas:", novas, "| rotas no total:", porTexto.size);
} else {
  console.log("uso: enviar <lista.json> | status | baixar | aplicar");
}
