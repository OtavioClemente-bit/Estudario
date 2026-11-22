// Troca as imagens da página da loja (pt-BR) na Play Console pela API oficial:
// ícone 512, capa 1024x500 e as capturas de celular 01..NN.png, na ordem.
// Uso: deno run -A scripts/play/imagens.ts work/loja-play-2026-11-22
// A chave fica em scripts/play/play-key.json (fora do git) e nunca é impressa.
const PACOTE = "br.com.estudario";
const LINGUA = "pt-BR";
const raiz = new URL("../../", import.meta.url);
const pasta = Deno.args[0];
if (!pasta) throw new Error("Informe a pasta das imagens");
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
const up = `https://androidpublisher.googleapis.com/upload/androidpublisher/v3/applications/${PACOTE}`;
const edicao = await ok(await fetch(`${api}/edits`, { method: "POST", headers: h }), "abrir edição");
const e = edicao.id;
async function trocar(tipo: string, arquivos: string[]) {
  await ok(await fetch(`${api}/edits/${e}/listings/${LINGUA}/${tipo}`, { method: "DELETE", headers: h }), `apagar ${tipo}`);
  for (const a of arquivos) {
    const bytes = await Deno.readFile(`${pasta}/${a}`);
    await ok(await fetch(`${up}/edits/${e}/listings/${LINGUA}/${tipo}?uploadType=media`, { method: "POST", headers: { ...h, "content-type": "image/png" }, body: bytes }), `enviar ${a}`);
    console.log(`${tipo}: ${a}`);
  }
}
const telas = [...Deno.readDirSync(pasta)].map((f) => f.name).filter((n) => /^\d\d\.png$/.test(n)).sort();
await trocar("icon", ["icone-512.png"]);
await trocar("featureGraphic", ["capa-1024x500.png"]);
await trocar("phoneScreenshots", telas);
await ok(await fetch(`${api}/edits/${e}:commit`, { method: "POST", headers: h }), "confirmar");
console.log("Página da loja atualizada.");
