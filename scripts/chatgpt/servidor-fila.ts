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
  if (req.method === "GET" && u.pathname === "/proximos") {
    const n = Math.min(Number(u.searchParams.get("n") ?? 10), 40);
    const o = await new Deno.Command("deno", { args: ["run", "-A", `${ROOT}scripts/chatgpt/fila.ts`, "proximos", String(n)], stdout: "piped" }).output();
    const linhas = new TextDecoder().decode(o.stdout).trim().split("\n").filter(Boolean);
    log(`entregues ${linhas.length} pedidos ao script`);
    return json(linhas);
  }
  if (req.method === "POST" && u.pathname === "/arquivo") {
    const { nome, conteudo } = await req.json().catch(() => ({}));
    // Só nomes simples (letras, números, ponto e hífen; nada de caminho).
    if (!/^[a-z0-9][a-z0-9.-]{3,150}$/.test(String(nome ?? "")) || String(nome).includes("..")) return json({ ok: false, erro: "nome inválido" }, 400);
    try { JSON.parse(conteudo); } catch { log(`${nome}: JSON inválido, não gravado`); return json({ ok: false, erro: "json inválido" }, 400); }
    const destino = `${SAIDA}/${nome}.json`;
    try { Deno.statSync(destino); return json({ ok: true, ja: true }); } catch { /* novo */ }
    Deno.writeTextFileSync(destino, conteudo);
    log(`gravado ${nome}.json`);
    return json({ ok: true });
  }
  return json({ ok: false }, 404);
});
log("servidor da fila iniciado em http://127.0.0.1:8788");
