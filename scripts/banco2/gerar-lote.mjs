// Gera lotes do banco 2 pela Message Batches API da Anthropic.
// Cada lote é um pedido independente: o modelo escreve as 50 questões numa resposta só.
// O script só envia, espera, grava a resposta e confere os números (verificar.js). Não altera questões.
//
// Uso:
//   node scripts/banco2/gerar-lote.mjs enviar 092:1 092:2 ...   (ou 092:1-6 para os 6 lotes)
//   node scripts/banco2/gerar-lote.mjs status
//   node scripts/banco2/gerar-lote.mjs baixar
//
// Chave em .env.anthropic (ANTHROPIC_API_KEY=...), fora do git.

import fs from "node:fs";
import path from "node:path";

const ROOT = path.resolve(path.dirname(new URL(import.meta.url).pathname.replace(/^\/(\w:)/, "$1")), "../..");
const FILA = path.join(ROOT, "conteudo/fila/banco-questoes-2");
const SAIDA = path.join(ROOT, "conteudo/entrada/banco2-api");
const ESTADO = path.join(SAIDA, "_batches.json");
const MODELO = process.env.BANCO2_MODELO || "claude-sonnet-5-5";
const MAX_TOKENS = 64000;

const ESTILOS = { 1: "FGV", 2: "FCC", 3: "VUNESP", 4: "CESGRANRIO", 5: "INSTITUTOS", 6: "DESAFIO" };

function workspace() {
  const t = fs.readFileSync(path.join(ROOT, ".env.anthropic"), "utf8");
  const m = t.match(/ANTHROPIC_WORKSPACE_ID=(S+)/);
  return m ? m[1] : null;
}

function chave() {
  const t = fs.readFileSync(path.join(ROOT, ".env.anthropic"), "utf8");
  const m = t.match(/ANTHROPIC_API_KEY=(\S+)/);
  if (!m) throw new Error("ANTHROPIC_API_KEY não encontrada em .env.anthropic");
  return m[1];
}

async function api(metodo, rota, corpo) {
  const r = await fetch("https://api.anthropic.com/v1/" + rota, {
    method: metodo,
    headers: { "x-api-key": chave(), "anthropic-version": "2023-06-01", "content-type": "application/json", ...(workspace() ? { "anthropic-workspace-id": workspace() } : {}) },
    body: corpo ? JSON.stringify(corpo) : undefined,
  });
  const txt = await r.text();
  if (!r.ok) throw new Error(`${metodo} ${rota}: ${r.status} ${txt.slice(0, 500)}`);
  return txt;
}

function ficha(nnn) {
  const f = fs.readdirSync(FILA).find((x) => x.startsWith(nnn + "-") && x.endsWith(".md"));
  if (!f) throw new Error("ficha não encontrada: " + nnn);
  return { id: f.slice(4, -3), texto: fs.readFileSync(path.join(FILA, f), "utf8") };
}

function regras() {
  // Versão com os 7 erros que reprovam lotes; sem pesquisa na web.
  const p = path.join(ROOT, "conteudo/entrada/00-REGRAS-v2-para-projeto-gpt.md");
  return fs.readFileSync(p, "utf8")
    .replace(/ Pesquise na web e use fontes oficiais \([^)]*\)\./, " Não pesquise na web: escreva com o que você já sabe; se não tiver certeza de um número de lei, artigo, prazo ou súmula, explique o conteúdo sem o número.")
    .replace(/ \(confira na web a vigência\)/, "");
}

function pedido(nnn, lote, metade) {
  const { id, texto } = ficha(nnn);
  const user = `${texto}

---
Faça agora: **BANCO ${nnn}, lote ${lote}, METADE ${metade} de 2** (estilo ${ESTILOS[lote]}), matéria \`${id}\`.
- Exatamente 25 questões nesta resposta (a outra metade do lote será escrita separadamente).
- ${metade === 1 ? "Use principalmente a PRIMEIRA metade da lista de tópicos da ficha" : "Use principalmente a SEGUNDA metade da lista de tópicos da ficha"}, com casos, textos e personagens próprios.
- Cada letra de A a E correta exatamente 5 vezes.
- A correta com tamanho parecido com as erradas: no máximo 8 vezes a mais longa e no máximo 8 vezes a mais curta.
- ${lote === 6 ? "Todas DIFICIL." : "Cerca de 8 MEDIA e 17 DIFICIL."}
- Responda SOMENTE com o objeto JSON (começando em { e terminando em }), sem texto antes ou depois e sem bloco de código.
- "materia": "${id}", "lote": ${lote}, "estilo": "${ESTILOS[lote]}".`;
  return {
    custom_id: `${nnn}-${lote}-${metade}`,
    params: { model: MODELO, max_tokens: MAX_TOKENS, thinking: JSON.parse(process.env.BANCO2_THINKING || "{\"type\":\"between_tools\"}"), ...(process.env.BANCO2_EFFORT ? { output_config: { effort: process.env.BANCO2_EFFORT } } : {}), system: regras(), messages: [{ role: "user", content: user }] },
  };
}

function lerEstado() { try { return JSON.parse(fs.readFileSync(ESTADO, "utf8")); } catch { return []; } }
function salvarEstado(e) { fs.mkdirSync(SAIDA, { recursive: true }); fs.writeFileSync(ESTADO, JSON.stringify(e, null, 2)); }

function alvos(args) {
  const out = [];
  for (const a of args) {
    const m = a.match(/^(\d{3}):(\d)(?:-(\d))?$/);
    if (!m) throw new Error("use NNN:L ou NNN:L-L, ex.: 092:1-6");
    for (let l = +m[2]; l <= +(m[3] || m[2]); l++) out.push([m[1], l]);
  }
  return out;
}

const [cmd, ...args] = process.argv.slice(2);

if (cmd === "enviar") {
  const reqs = alvos(args).flatMap(([n, l]) => [pedido(n, l, 1), pedido(n, l, 2)]);
  const r = JSON.parse(await api("POST", "messages/batches", { requests: reqs }));
  const e = lerEstado();
  e.push({ id: r.id, criado: new Date().toISOString(), modelo: MODELO, itens: reqs.map((x) => x.custom_id), baixado: false });
  salvarEstado(e);
  console.log("batch enviado:", r.id, "| pedidos:", reqs.length, "| modelo:", MODELO);
} else if (cmd === "status") {
  for (const b of lerEstado().filter((x) => !x.baixado)) {
    const r = JSON.parse(await api("GET", "messages/batches/" + b.id));
    console.log(b.id, r.processing_status, JSON.stringify(r.request_counts));
  }
} else if (cmd === "baixar") {
  const e = lerEstado();
  for (const b of e.filter((x) => !x.baixado)) {
    const st = JSON.parse(await api("GET", "messages/batches/" + b.id));
    if (st.processing_status !== "ended") { console.log(b.id, "ainda", st.processing_status); continue; }
    const linhas = (await api("GET", `messages/batches/${b.id}/results`)).trim().split("\n");
    let tin = 0, tout = 0;
    for (const ln of linhas) {
      const r = JSON.parse(ln);
      const [nnn, lote, metade] = r.custom_id.split("-");
      if (r.result.type !== "succeeded") { console.log(r.custom_id, "FALHOU:", JSON.stringify(r.result).slice(0, 300)); continue; }
      const msg = r.result.message;
      tin += msg.usage.input_tokens; tout += msg.usage.output_tokens;
      let txt = msg.content.filter((c) => c.type === "text").map((c) => c.text).join("").trim();
      txt = txt.replace(/^```(?:json)?\s*/, "").replace(/```\s*$/, "");
      const { id } = ficha(nnn);
      const arq = path.join(SAIDA, `${id}.banco2-${lote}.parte${metade}.json`);
      try { JSON.parse(txt); fs.writeFileSync(arq, txt); console.log(r.custom_id, "gravado", path.basename(arq), "| stop:", msg.stop_reason, "| saída:", msg.usage.output_tokens, "tokens"); }
      catch { fs.writeFileSync(arq + ".invalido.txt", txt); console.log(r.custom_id, "JSON INVÁLIDO (stop:", msg.stop_reason + ")"); }
    }
    // junta as metades prontas
    for (const f of fs.readdirSync(SAIDA).filter((x) => x.endsWith(".parte1.json"))) {
      const p2 = path.join(SAIDA, f.replace(".parte1.", ".parte2."));
      if (!fs.existsSync(p2)) continue;
      const a = JSON.parse(fs.readFileSync(path.join(SAIDA, f), "utf8")), c = JSON.parse(fs.readFileSync(p2, "utf8"));
      a.questions = a.questions.concat(c.questions);
      fs.writeFileSync(path.join(SAIDA, f.replace(".parte1.json", ".json")), JSON.stringify(a, null, 1));
      console.log("juntado", f.replace(".parte1.json", ".json"), a.questions.length, "questões");
    }
    b.baixado = true; b.tokens = { entrada: tin, saida: tout };
    console.log(b.id, "tokens: entrada", tin, "saída", tout);
  }
  salvarEstado(e);
} else {
  console.log("uso: enviar NNN:L[-L] ... | status | baixar");
}
