// Divide tópicos-pacote de edital (vários assuntos num item) em subtópicos de um assunto só, cada
// um ligado à matéria que o ensina (ou a nenhuma), pela Message Batches API. O texto dos subtópicos
// sai das palavras do próprio edital: nada é acrescentado nem perdido, só separado.
//
// Uso:
//   node scripts/biblioteca/ligar-dividir.mjs selecionar            (grava a lista em conteudo/entrada/ligacoes-api/divisao/lista.json)
//   node scripts/biblioteca/ligar-dividir.mjs enviar [--por 60]
//   node scripts/biblioteca/ligar-dividir.mjs status
//   node scripts/biblioteca/ligar-dividir.mjs baixar
//   node scripts/biblioteca/ligar-dividir.mjs aplicar <pasta-editais> [<pasta> ...]

import fs from "node:fs";
import path from "node:path";

const ROOT = path.resolve(path.dirname(new URL(import.meta.url).pathname.replace(/^\/(\w:)/, "$1")), "../..");
const SAIDA = path.join(ROOT, "conteudo/entrada/ligacoes-api/divisao");
const ESTADO = path.join(SAIDA, "_batches.json");
const MODELO = process.env.LIGAR_MODELO || "claude-sonnet-5-5";
const PASTAS = ["conteudo/editais", "conteudo/entrada/editais-novos"].map((p) => path.join(ROOT, p)).filter((p) => fs.existsSync(p));

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
      lista.push(`${m.id} | ${m.title} — ${String(m.scope?.covers ?? "").replace(/\s+/g, " ").slice(0, 150)}`);
    }
  }
  return lista.sort();
}
const partes = (texto) => texto.split(/[;.]\s+(?=[A-ZÁÉÍÓÚÂÊÔÃÕÇ0-9])|,\s+(?=[A-ZÁÉÍÓÚ])/).filter((s) => s.trim().length > 10).length;

const SISTEMA = (cat) => `Você prepara tópicos de editais de concurso para um aplicativo de estudo. Cada tópico precisa tratar de UM assunto só, para o aluno estudar uma matéria completa por tópico.

Você recebe tópicos que juntam vários assuntos. Para cada um, divida em subtópicos:
- Use as PALAVRAS DO PRÓPRIO EDITAL. Não acrescente assunto, não resuma a ponto de perder conteúdo, não reescreva. Todo o conteúdo do tópico original tem de estar em algum subtópico.
- Um subtópico = um assunto que seria uma matéria de estudo. Itens que pertencem ao mesmo assunto ficam juntos (ex.: "Ato administrativo: conceito, requisitos, atributos" é um assunto só).
- Se o tópico já trata de um assunto só, devolva um único subtópico com o texto original.
- Cada subtópico: começa com maiúscula, termina com ponto, de 3 a 400 caracteres, sem numeração ("1.", "a)"). Se o assunto depende do contexto do tópico (ex.: "Recursos." dentro de processo civil), deixe o texto autoexplicativo usando palavras do original.
- Para cada subtópico, escolha a matéria do catálogo que ENSINA aquele assunto inteiro ("m"), ou null se nenhuma ensina. "c": true se a matéria cobre o subtópico inteiro; false se cobre só parte.
- Lei estadual/municipal, regimento, estatuto de um ente: só ligue a matéria específica daquele ente; senão null.
- Use só ids do catálogo, exatamente como escritos.

Responda SOMENTE com JSON: {"r":[{"i":<n do tópico>,"s":[{"t":"<texto do subtópico>","m":"<id>"|null,"c":true|false}]}]} — uma entrada por tópico, na ordem.

CATÁLOGO (id | título — escopo):
${cat}`;

function lerEstado() { try { return JSON.parse(fs.readFileSync(ESTADO, "utf8")); } catch { return []; } }
function salvarEstado(e) { fs.mkdirSync(SAIDA, { recursive: true }); fs.writeFileSync(ESTADO, JSON.stringify(e, null, 2)); }
const arg = (nome, padrao) => { const i = process.argv.indexOf(nome); return i > 0 ? process.argv[i + 1] : padrao; };
const [cmd, ...resto] = process.argv.slice(2);

if (cmd === "selecionar") {
  const rotas = new Set(JSON.parse(fs.readFileSync(path.join(ROOT, "conteudo/pedidos/rotas.json"), "utf8")).map((r) => r.texto));
  const mapa = new Map();
  for (const pasta of PASTAS) for (const f of fs.readdirSync(pasta).filter((x) => x.endsWith(".json"))) {
    const ed = JSON.parse(fs.readFileSync(path.join(pasta, f), "utf8"));
    for (const d of ed.disciplinas) for (const t of d.topicos) {
      if (!(rotas.has(t.texto) || partes(t.texto) >= 3)) continue;
      const k = `${d.nome}\u0001${t.texto}`;
      if (!mapa.has(k)) mapa.set(k, { disciplina: d.nome, texto: t.texto });
    }
  }
  fs.mkdirSync(SAIDA, { recursive: true });
  fs.writeFileSync(path.join(SAIDA, "lista.json"), JSON.stringify([...mapa.values()]));
  console.log("tópicos para dividir:", mapa.size);
} else if (cmd === "enviar") {
  const itens = JSON.parse(fs.readFileSync(path.join(SAIDA, "lista.json"), "utf8"));
  const por = Number(arg("--por", 60));
  const inicio = Number(arg("--inicio", 0));
  const limite = Number(arg("--limite", itens.length));
  const sel = itens.slice(inicio, inicio + limite);
  const sistema = SISTEMA(materias().join("\n"));
  const reqs = [];
  const base = Date.now().toString(36);
  for (let p = 0; p < sel.length; p += por) {
    const parte = sel.slice(p, p + por);
    const id = `div-${base}-${String(p / por).padStart(4, "0")}`;
    fs.writeFileSync(path.join(SAIDA, `${id}.pedido.json`), JSON.stringify(parte));
    const corpo = parte.map((t, i) => `${i + 1}. [${t.disciplina}] ${t.texto}`).join("\n");
    reqs.push({ custom_id: id, params: { model: MODELO, max_tokens: 32000, thinking: { type: "between_tools" }, system: [{ type: "text", text: sistema, cache_control: { type: "ephemeral" } }], messages: [{ role: "user", content: `Tópicos:\n${corpo}` }] } });
  }
  const r = JSON.parse(await api("POST", "messages/batches", { requests: reqs }));
  const e = lerEstado();
  e.push({ id: r.id, criado: new Date().toISOString(), modelo: MODELO, topicos: sel.length, pedidos: reqs.length, baixado: false });
  salvarEstado(e);
  console.log("divisão enviada:", r.id, "| tópicos:", sel.length, "| pedidos:", reqs.length);
} else if (cmd === "status") {
  for (const b of lerEstado().filter((x) => !x.baixado)) {
    const r = JSON.parse(await api("GET", "messages/batches/" + b.id));
    console.log(b.id, r.processing_status, JSON.stringify(r.request_counts));
  }
} else if (cmd === "baixar") {
  const ids = new Set(materias().map((l) => l.split(" | ")[0]));
  const arq = path.join(SAIDA, "divisao.json");
  const todas = fs.existsSync(arq) ? JSON.parse(fs.readFileSync(arq, "utf8")) : [];
  const e = lerEstado();
  for (const b of e.filter((x) => !x.baixado)) {
    const st = JSON.parse(await api("GET", "messages/batches/" + b.id));
    if (st.processing_status !== "ended") { console.log(b.id, "ainda", st.processing_status); continue; }
    const linhas = (await api("GET", `messages/batches/${b.id}/results`)).trim().split("\n");
    const u = { entrada: 0, cache: 0, saida: 0 };
    let tops = 0, subs = 0, ruins = 0;
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
        if (!t || !Array.isArray(x.s) || x.s.length === 0) { ruins++; continue; }
        const s = x.s
          .map((y) => ({ t: String(y.t ?? "").trim(), m: y.m && ids.has(y.m) ? y.m : null, c: y.c !== false }))
          .filter((y) => y.t.length >= 3 && y.t.length <= 400);
        if (s.length === 0) { ruins++; continue; }
        todas.push({ disciplina: t.disciplina, texto: t.texto, sub: s });
        tops++; subs += s.length;
      }
    }
    b.baixado = true; b.uso = u;
    console.log(b.id, `| tópicos ${tops} → subtópicos ${subs} | ruins ${ruins} | tokens entrada ${u.entrada} cache ${u.cache} saída ${u.saida}`);
  }
  fs.writeFileSync(arq, JSON.stringify(todas, null, 1));
  salvarEstado(e);
} else if (cmd === "aplicar") {
  const todas = JSON.parse(fs.readFileSync(path.join(SAIDA, "divisao.json"), "utf8"));
  const mapa = new Map(todas.map((x) => [`${x.disciplina}\u0001${x.texto}`, x.sub]));
  const pastas = resto.length ? resto.map((p) => path.join(ROOT, p)) : PASTAS;
  let trocados = 0, novos = 0;
  const divididos = new Set();
  for (const pasta of pastas) for (const f of fs.readdirSync(pasta).filter((x) => x.endsWith(".json"))) {
    const p = path.join(pasta, f);
    const ed = JSON.parse(fs.readFileSync(p, "utf8"));
    let mudou = false;
    for (const d of ed.disciplinas) {
      const saida = [];
      const vistos = new Set();
      for (const t of d.topicos) {
        const sub = mapa.get(`${d.nome}\u0001${t.texto}`);
        if (!sub || sub.length === 1 && sub[0].t === t.texto && (sub[0].m ?? null) === t.topico) {
          if (!vistos.has(t.texto)) { saida.push(t); vistos.add(t.texto); }
          continue;
        }
        divididos.add(t.texto);
        trocados++;
        for (const s of sub) {
          if (vistos.has(s.t)) continue;
          vistos.add(s.t);
          saida.push({ ...t, texto: s.t, topico: s.m ?? null });
          novos++;
        }
        mudou = true;
      }
      d.topicos = saida;
    }
    if (mudou) fs.writeFileSync(p, JSON.stringify(ed, null, 2) + "\n");
  }
  // As rotas desses textos deixam de ser necessárias: cada subtópico tem a sua matéria.
  const rotasPath = path.join(ROOT, "conteudo/pedidos/rotas.json");
  const rotas = JSON.parse(fs.readFileSync(rotasPath, "utf8"));
  const ficam = rotas.filter((r) => !divididos.has(r.texto));
  fs.writeFileSync(rotasPath, JSON.stringify(ficam, null, 1) + "\n");
  console.log("tópicos divididos:", trocados, "| subtópicos gravados:", novos, "| rotas:", rotas.length, "->", ficam.length);
} else {
  console.log("uso: selecionar | enviar [--por 60] | status | baixar | aplicar [pastas]");
}
