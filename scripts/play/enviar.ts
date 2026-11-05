// Envia o AAB do Estudário para a Play Console pela API oficial (Google Play Developer API v3).
//
// Uso:
//   deno run -A scripts/play/enviar.ts                      -> o AAB mais novo, faixa interna, rascunho
//   deno run -A scripts/play/enviar.ts --faixa production   -> produção (fica em RASCUNHO: você publica na Console)
//   deno run -A scripts/play/enviar.ts --faixa production --publicar   -> manda já para revisão/lançamento
//   deno run -A scripts/play/enviar.ts --aab caminho.aab --notas "texto das novidades"
//
// A chave da conta de serviço fica em scripts/play/play-key.json (fora do git, ver .gitignore).
// Ela é lida aqui e nunca impressa.

const PACOTE = "br.com.estudario";
const raiz = new URL("../../", import.meta.url);
const args = Deno.args;
const arg = (nome: string) => { const i = args.indexOf(nome); return i >= 0 ? args[i + 1] : undefined; };
const faixa = arg("--faixa") ?? "internal";
const publicar = args.includes("--publicar");
const notas = arg("--notas") ?? "Melhorias de visual e correções.";

// o AAB: o pedido, ou o mais novo da pasta de saída
let aab = arg("--aab");
if (!aab) {
  const pasta = new URL("app/build/outputs/bundle/release/", raiz);
  let melhor: { nome: string; t: number } | null = null;
  for await (const e of Deno.readDir(pasta)) {
    if (!e.isFile || !e.name.startsWith("estudario-") || !e.name.endsWith(".aab")) continue;
    const t = (await Deno.stat(new URL(e.name, pasta))).mtime?.getTime() ?? 0;
    if (!melhor || t > melhor.t) melhor = { nome: e.name, t };
  }
  if (!melhor) throw new Error("Nenhum estudario-*.aab em app/build/outputs/bundle/release/");
  aab = new URL(melhor.nome, pasta).pathname.replace(/^\/([A-Za-z]:)/, "$1");
}
console.log(`AAB: ${aab}\nFaixa: ${faixa} · ${publicar ? "publicar" : "rascunho"}`);

// login da conta de serviço (JWT assinado com a chave, trocado por um token de acesso)
const chave = JSON.parse(await Deno.readTextFile(new URL("scripts/play/play-key.json", raiz)));
const b64 = (d: Uint8Array | string) => btoa(typeof d === "string" ? d : String.fromCharCode(...d)).replace(/=+$/, "").replace(/\+/g, "-").replace(/\//g, "_");
const agora = Math.floor(Date.now() / 1000);
const corpo = `${b64(JSON.stringify({ alg: "RS256", typ: "JWT" }))}.${b64(JSON.stringify({
  iss: chave.client_email, scope: "https://www.googleapis.com/auth/androidpublisher",
  aud: "https://oauth2.googleapis.com/token", iat: agora, exp: agora + 3600,
}))}`;
const pem = chave.private_key.replace(/-----[^-]+-----/g, "").replace(/\s+/g, "");
const der = Uint8Array.from(atob(pem), (c) => c.charCodeAt(0));
const k = await crypto.subtle.importKey("pkcs8", der, { name: "RSASSA-PKCS1-v1_5", hash: "SHA-256" }, false, ["sign"]);
const assinatura = new Uint8Array(await crypto.subtle.sign("RSASSA-PKCS1-v1_5", k, new TextEncoder().encode(corpo)));
const tok = await (await fetch("https://oauth2.googleapis.com/token", {
  method: "POST", headers: { "content-type": "application/x-www-form-urlencoded" },
  body: new URLSearchParams({ grant_type: "urn:ietf:params:oauth:grant-type:jwt-bearer", assertion: `${corpo}.${b64(assinatura)}` }),
})).json();
if (!tok.access_token) throw new Error("Login da conta de serviço falhou: " + JSON.stringify(tok));
const h = { Authorization: `Bearer ${tok.access_token}` };
const api = `https://androidpublisher.googleapis.com/androidpublisher/v3/applications/${PACOTE}`;
async function ok(r: Response, oque: string) { if (!r.ok) throw new Error(`${oque}: ${r.status} ${await r.text()}`); return r.json(); }

// 1) abre uma edição, 2) sobe o AAB, 3) põe na faixa, 4) confirma
const edicao = await ok(await fetch(`${api}/edits`, { method: "POST", headers: h }), "abrir edição");
console.log("Enviando o AAB (pode levar alguns minutos)...");
const bundle = await ok(await fetch(`https://androidpublisher.googleapis.com/upload/androidpublisher/v3/applications/${PACOTE}/edits/${edicao.id}/bundles?uploadType=media`, {
  method: "POST", headers: { ...h, "content-type": "application/octet-stream" }, body: await Deno.readFile(aab),
}), "enviar AAB");
console.log(`AAB aceito: versionCode ${bundle.versionCode}`);
await ok(await fetch(`${api}/edits/${edicao.id}/tracks/${faixa}`, {
  method: "PUT", headers: { ...h, "content-type": "application/json" },
  body: JSON.stringify({ track: faixa, releases: [{ versionCodes: [String(bundle.versionCode)], status: publicar ? "completed" : "draft", releaseNotes: [{ language: "pt-BR", text: notas }] }] }),
}), "pôr na faixa");
await ok(await fetch(`${api}/edits/${edicao.id}:commit`, { method: "POST", headers: h }), "confirmar");
console.log(`Pronto: versionCode ${bundle.versionCode} na faixa "${faixa}" (${publicar ? "enviado para lançamento" : "rascunho: falta publicar na Play Console"}).`);
