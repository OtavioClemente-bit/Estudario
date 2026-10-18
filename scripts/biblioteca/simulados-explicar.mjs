// Enriquece as explicações curtas dos simulados (conteudo/simulados) pela Message Batches API.
// O modelo escreve SÓ a explicação; enunciado, alternativas e gabarito não mudam. Se achar o gabarito
// errado, aponta em "alerta" e a explicação não é trocada (revisão humana).
//
//   node scripts/biblioteca/simulados-explicar.mjs enviar [--por 40]
//   node scripts/biblioteca/simulados-explicar.mjs status
//   node scripts/biblioteca/simulados-explicar.mjs baixar      (grava nos simulados e lista os alertas)

import fs from "node:fs";
import path from "node:path";

const ROOT = path.resolve(path.dirname(new URL(import.meta.url).pathname.replace(/^\/(\w:)/, "$1")), "../..");
const SIM = path.join(ROOT, "conteudo/simulados");
const SAIDA = path.join(ROOT, "conteudo/entrada/simulados-explicar");
const ESTADO = path.join(SAIDA, "_batches.json");
const MODELO = process.env.EXPLICAR_MODELO || "claude-opus-5-5";
const MIN = 150;

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
const curta = (q) => (q.explanation ?? "").length + (q.pegadinha ? q.pegadinha.length + 12 : 0) < 120;
const lerEstado = () => { try { return JSON.parse(fs.readFileSync(ESTADO, "utf8")); } catch { return []; } };
const salvarEstado = (e) => { fs.mkdirSync(SAIDA, { recursive: true }); fs.writeFileSync(ESTADO, JSON.stringify(e, null, 2)); };
const arg = (n, d) => { const i = process.argv.indexOf(n); return i > 0 ? process.argv[i + 1] : d; };

const SISTEMA = `Você é professor de cursinho preparatório de alto nível para concursos públicos no Brasil.
Para cada questão, escreva a EXPLICAÇÃO que o aluno lê depois de responder, no padrão dos melhores cursinhos:
- 2 a 4 frases, entre ${MIN} e 600 caracteres, em português claro.
- Diga por que a resposta correta está correta, citando a regra, o conceito ou o dispositivo que resolve a questão; em múltipla escolha, aponte em uma frase o erro das alternativas mais tentadoras; em Certo/Errado, diga exatamente o que torna o item certo ou errado.
- Termine, quando couber, com a armadilha que derruba o candidato.
- Use a explicação curta e a pegadinha recebidas como base; não contradiga o gabarito.
- Não cite número de lei, artigo, súmula ou tema de que não tenha certeza: explique o conteúdo sem o número.
- NÃO altere enunciado, alternativas nem gabarito. Se tiver CERTEZA de que o gabarito está errado ou de que há duas respostas corretas, não escreva explicação: preencha "alerta" com o motivo.
Responda SOMENTE com JSON: {"r":[{"i":<n>,"explicacao":"..."}]} — ou {"i":<n>,"alerta":"..."} quando for o caso. Uma entrada por questão, na ordem.`;

const [cmd] = process.argv.slice(2);

if (cmd === "enviar") {
  const por = Number(arg("--por", 40));
  const itens = [];
  for (const f of fs.readdirSync(SIM).filter((x) => /\.simulado-\d+\.json$/.test(x))) {
    const j = JSON.parse(fs.readFileSync(path.join(SIM, f), "utf8"));
    j.questions.forEach((q, k) => { if (curta(q)) itens.push({ f, k, banca: j.banca, q }); });
  }
  fs.mkdirSync(SAIDA, { recursive: true });
  const reqs = [];
  const base = Date.now().toString(36);
  for (let p = 0; p < itens.length; p += por) {
    const parte = itens.slice(p, p + por);
    const id = `exp-${base}-${String(p / por).padStart(4, "0")}`;
    fs.writeFileSync(path.join(SAIDA, `${id}.pedido.json`), JSON.stringify(parte.map(({ f, k }) => ({ f, k }))));
    const corpo = parte.map(({ banca, q }, i) => {
      const ops = q.options.map((o) => `${o.correct ? "*" : " "}${o.key}) ${o.text}`).join("\n");
      return `#${i + 1} [${banca} · ${q.disciplina} · ${q.topico ?? ""}]\n${q.textoBase ? "Texto: " + q.textoBase.slice(0, 1500) + "\n" : ""}${q.statement}\n${ops}\nExplicação curta: ${q.explanation ?? ""}\nPegadinha: ${q.pegadinha ?? ""}`;
    }).join("\n\n");
    reqs.push({ custom_id: id, params: { model: MODELO, max_tokens: 24000, thinking: { type: "adaptive" }, output_config: { effort: "low" }, system: SISTEMA, messages: [{ role: "user", content: corpo }] } });
  }
  const r = JSON.parse(await api("POST", "messages/batches", { requests: reqs }));
  const e = lerEstado();
  e.push({ id: r.id, criado: new Date().toISOString(), modelo: MODELO, questoes: itens.length, pedidos: reqs.length, baixado: false });
  salvarEstado(e);
  console.log("enviado:", r.id, "| questões:", itens.length, "| pedidos:", reqs.length);
} else if (cmd === "status") {
  for (const b of lerEstado().filter((x) => !x.baixado)) {
    const r = JSON.parse(await api("GET", "messages/batches/" + b.id));
    console.log(b.id, r.processing_status, JSON.stringify(r.request_counts));
  }
} else if (cmd === "baixar") {
  const e = lerEstado();
  const alertas = [];
  const sims = new Map();
  const sim = (f) => { if (!sims.has(f)) sims.set(f, JSON.parse(fs.readFileSync(path.join(SIM, f), "utf8"))); return sims.get(f); };
  let trocadas = 0, curtas2 = 0;
  for (const b of e.filter((x) => !x.baixado)) {
    const st = JSON.parse(await api("GET", "messages/batches/" + b.id));
    if (st.processing_status !== "ended") { console.log(b.id, "ainda", st.processing_status); continue; }
    const linhas = (await api("GET", `messages/batches/${b.id}/results`)).trim().split("\n");
    let tin = 0, tout = 0;
    for (const ln of linhas) {
      const r = JSON.parse(ln);
      if (r.result.type !== "succeeded") { console.log(r.custom_id, "FALHOU"); continue; }
      const msg = r.result.message; tin += msg.usage.input_tokens; tout += msg.usage.output_tokens;
      const parte = JSON.parse(fs.readFileSync(path.join(SAIDA, `${r.custom_id}.pedido.json`), "utf8"));
      const txt = msg.content.filter((c) => c.type === "text").map((c) => c.text).join("").trim().replace(/^```(?:json)?\s*/, "").replace(/```\s*$/, "");
      let resp; try { resp = JSON.parse(txt).r; } catch { console.log(r.custom_id, "resposta inválida"); continue; }
      for (const x of resp) {
        const ref = parte[x.i - 1]; if (!ref) continue;
        const q = sim(ref.f).questions[ref.k];
        if (x.alerta) { alertas.push({ simulado: ref.f, n: q.n ?? ref.k + 1, alerta: x.alerta, statement: q.statement.slice(0, 300) }); continue; }
        const ex = String(x.explicacao ?? "").trim();
        if (ex.length < MIN || ex.length > 900) { curtas2++; continue; }
        q.explanationOriginal ??= q.explanation;
        q.explanation = ex;
        trocadas++;
      }
    }
    b.baixado = true; b.tokens = { entrada: tin, saida: tout };
    console.log(b.id, "| tokens entrada", tin, "saída", tout);
  }
  for (const [f, j] of sims) fs.writeFileSync(path.join(SIM, f), JSON.stringify(j, null, 2) + "\n");
  fs.writeFileSync(path.join(SAIDA, "alertas.json"), JSON.stringify(alertas, null, 1));
  salvarEstado(e);
  console.log(`explicações trocadas: ${trocadas} | fora do tamanho: ${curtas2} | alertas de gabarito: ${alertas.length}`);
} else {
  console.log("uso: enviar [--por 40] | status | baixar");
}
