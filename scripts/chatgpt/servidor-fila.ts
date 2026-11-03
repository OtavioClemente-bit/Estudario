// Ponte entre o script do Tampermonkey (no Chrome, na página do ChatGPT) e o projeto. Só escuta em 127.0.0.1.
//   GET  /proximos?n=30  → próximos pedidos da fila (marca como enviados e reserva as matérias)
//   POST /arquivo        → {nome, conteudo}: grava o JSON entregue pelo ChatGPT em conteudo/entrada/chatgpt/
//   deno run -A scripts/chatgpt/servidor-fila.ts
const ROOT = new URL("../../", import.meta.url).pathname.replace(/^\/([A-Za-z]:)/, "$1");
const SAIDA = `${ROOT}conteudo/entrada/chatgpt`;
const LOG = `${SAIDA}/automatico-log.txt`;
const log = (m: string) => {
  const l = `[${new Date().toLocaleString("pt-BR")}] ${m}`;
  console.log(l);
  Deno.writeTextFileSync(LOG, l + "\n", { append: true });
};
const json = (x: unknown, status = 200) => new Response(JSON.stringify(x), { status, headers: { "Content-Type": "application/json; charset=utf-8" } });

Deno.serve({ hostname: "127.0.0.1", port: 8788 }, async (req) => {
  const u = new URL(req.url);
  // Os scripts do Tampermonkey se atualizam sozinhos por aqui (@updateURL), sem colar código à mão.
  if (req.method === "GET" && /^\/(estudario-[a-z0-9-]+\.(user|meta)\.js)$/.test(u.pathname)) {
    try {
      const nome = u.pathname.slice(1).replace(".meta.js", ".user.js");
      return new Response(await Deno.readTextFile(`${ROOT}scripts/chatgpt/${nome}`), { headers: { "content-type": "text/javascript; charset=utf-8" } });
    } catch { return new Response("não achei", { status: 404 }); }
  }
  if (req.method === "GET" && u.pathname === "/proximos") {
    const n = Math.min(Number(u.searchParams.get("n") ?? 10), 40);
    const o = await new Deno.Command("deno", { args: ["run", "-A", `${ROOT}scripts/chatgpt/fila.ts`, "proximos", String(n), ...(u.searchParams.get("tipo") === "QUESTOES" ? ["QUESTOES"] : [])], stdout: "piped" }).output();
    // Nos pedidos de múltipla escolha, lembra de espalhar o gabarito (o Gemini concentrava quase tudo em A e B).
    const OBS = "; OBS: distribua o gabarito por igual entre A, B, C, D e E (cerca de 1/5 cada) e a alternativa correta não pode ser sempre a mais longa";
    const linhas = new TextDecoder().decode(o.stdout).trim().split("\n").filter(Boolean)
      .map((l) => /^QUESTÕES:/.test(l) && !/formato=Certo\/Errado/i.test(l) ? l + OBS : l);
    log(`entregues ${linhas.length} pedidos ao script`);
    return json(linhas);
  }
  if (req.method === "POST" && u.pathname === "/arquivo") {
    const { nome, conteudo } = await req.json().catch(() => ({}));
    // Só nomes simples (letras, números, ponto e hífen; nada de caminho).
    if (!/^[a-z0-9][a-z0-9.-]{3,150}$/.test(String(nome ?? "")) || String(nome).includes("..")) return json({ ok: false, erro: "nome inválido" }, 400);
    try { JSON.parse(conteudo); } catch { log(`${nome}: JSON inválido, não gravado`); return json({ ok: false, erro: "json inválido" }, 400); }
    // Explicações das questões de prova real vão para a pasta delas (o juntar.ts não as confunde com questões).
    const destino = String(nome).startsWith("oficial-") ? `${ROOT}conteudo/entrada/oficiais/respostas/${nome}.json` : `${SAIDA}/${nome}.json`;
    try { Deno.statSync(destino); return json({ ok: true, ja: true }); } catch { /* novo */ }
    Deno.writeTextFileSync(destino, conteudo);
    log(`gravado ${nome}.json`);
    return json({ ok: true });
  }
  return json({ ok: false }, 404);
});
log("servidor da fila iniciado em http://127.0.0.1:8788");
