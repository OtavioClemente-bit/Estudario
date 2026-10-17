// Auditoria de lotes do banco 2 pela Message Batches API: o modelo só LÊ as questões e devolve a
// lista de problemas (gabarito errado, duas corretas, lei revogada, dado inventado...). Não altera
// nada: a correção é feita depois, à mão, conferindo cada apontamento.
//
// Uso:
//   node scripts/banco2/auditar.mjs enviar <pasta-dos-lotes> <id-da-materia> [<id> ...]
//   node scripts/banco2/auditar.mjs baixar
//
// Lê <pasta>/<id>.banco2-<L>.json (L = 1..6) e grava conteudo/entrada/auditoria/<id>.banco2-<L>.json

import fs from "node:fs";
import path from "node:path";

const ROOT = path.resolve(path.dirname(new URL(import.meta.url).pathname.replace(/^\/(\w:)/, "$1")), "../..");
const SAIDA = path.join(ROOT, "conteudo/entrada/auditoria");
const ESTADO = path.join(SAIDA, "_batches.json");
const MODELO = process.env.AUDITORIA_MODELO || "claude-opus-5-5";
const EFFORT = process.env.AUDITORIA_EFFORT || "medium";

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

const SISTEMA = `Você é revisor técnico de questões de concurso público no Brasil (nível dos melhores cursinhos).
Hoje é ${new Date().toISOString().slice(0, 10)}. Sua tarefa é só APONTAR problemas reais; não reescreva questões boas.

Para cada questão, verifique:
1. GABARITO_ERRADO: a alternativa marcada como correta está errada, e outra é a certa.
2. DUAS_CORRETAS: mais de uma alternativa é defensável como correta.
3. NENHUMA_CORRETA: nenhuma alternativa está certa.
4. LEI_DESATUALIZADA: a questão se baseia em norma revogada, alterada ou em entendimento superado (ex.: Lei 8.666 no lugar da 14.133, súmula cancelada, tese do STF/STJ que mudou).
5. DADO_ERRADO: número de artigo, prazo, valor, percentual, quórum ou nome de instituto incorreto.
6. EXPLICACAO_ERRADA: o gabarito está certo, mas a explicação afirma algo falso.
7. AMBIGUA: o enunciado permite leituras que mudam a resposta.

Regras:
- Só aponte o que você tem segurança de que está errado. Na dúvida sobre fato muito recente (de 2025 ou 2026) que você não conhece, NÃO aponte como erro; no máximo use o tipo AMBIGUA com a observação "conferir norma recente".
- Não aponte estilo, tamanho das alternativas, gramática leve ou preferência de redação.
- Responda SOMENTE com JSON: {"problemas":[{"n":<número da questão>,"tipo":"<um dos tipos>","gravidade":"ALTA|MEDIA","explicacao":"<o erro, em 1 a 3 frases, citando a regra>","correcao":"<o que deve mudar: letra certa, dado certo ou trecho a corrigir>"}]}
- Se não houver problema, responda {"problemas":[]}.`;

function compacta(lote) {
  return lote.questions.map((q, i) => {
    const ops = q.options.map((o) => `${o.correct ? "*" : " "}${o.key}) ${o.text}`).join("\n");
    return `#${i + 1} [${q.topico ?? ""}]\n${q.statement}\n${ops}\nExplicação: ${q.explanation}`;
  }).join("\n\n");
}

function lerEstado() { try { return JSON.parse(fs.readFileSync(ESTADO, "utf8")); } catch { return []; } }
function salvarEstado(e) { fs.mkdirSync(SAIDA, { recursive: true }); fs.writeFileSync(ESTADO, JSON.stringify(e, null, 2)); }

const [cmd, ...args] = process.argv.slice(2);

if (cmd === "enviar") {
  const [pasta, ...ids] = args;
  const reqs = [];
  for (const id of ids) {
    for (let l = 1; l <= 6; l++) {
      const arq = path.join(pasta, `${id}.banco2-${l}.json`);
      if (!fs.existsSync(arq)) continue;
      const lote = JSON.parse(fs.readFileSync(arq, "utf8"));
      reqs.push({
        custom_id: `${id.replace(/[^a-zA-Z0-9_-]/g, "_").slice(0, 55)}-${l}`,
        params: {
          model: MODELO, max_tokens: 32000,
          thinking: { type: "adaptive" }, output_config: { effort: EFFORT },
          system: SISTEMA,
          messages: [{ role: "user", content: `Matéria: ${id} — lote ${l}\n\n${compacta(lote)}` }],
        },
      });
    }
  }
  const r = JSON.parse(await api("POST", "messages/batches", { requests: reqs }));
  const e = lerEstado();
  e.push({ id: r.id, criado: new Date().toISOString(), modelo: MODELO, effort: EFFORT, ids, itens: reqs.map((x) => x.custom_id), baixado: false });
  salvarEstado(e);
  console.log("auditoria enviada:", r.id, "| lotes:", reqs.length);
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
    let tin = 0, tout = 0, total = 0;
    for (const ln of linhas) {
      const r = JSON.parse(ln);
      if (r.result.type !== "succeeded") { console.log(r.custom_id, "FALHOU:", JSON.stringify(r.result).slice(0, 300)); continue; }
      const msg = r.result.message;
      tin += msg.usage.input_tokens; tout += msg.usage.output_tokens;
      let txt = msg.content.filter((c) => c.type === "text").map((c) => c.text).join("").trim().replace(/^```(?:json)?\s*/, "").replace(/```\s*$/, "");
      const arq = path.join(SAIDA, `${r.custom_id}.json`);
      try { const j = JSON.parse(txt); fs.writeFileSync(arq, JSON.stringify(j, null, 1)); total += j.problemas.length; console.log(r.custom_id, "problemas:", j.problemas.length); }
      catch { fs.writeFileSync(arq + ".invalido.txt", txt); console.log(r.custom_id, "resposta inválida (stop:", msg.stop_reason + ")"); }
    }
    b.baixado = true; b.tokens = { entrada: tin, saida: tout };
    console.log(b.id, "| problemas:", total, "| tokens entrada", tin, "saída", tout);
  }
  salvarEstado(e);
} else {
  console.log("uso: enviar <pasta> <id>... | status | baixar");
}
